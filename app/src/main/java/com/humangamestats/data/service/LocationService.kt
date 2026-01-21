package com.humangamestats.data.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.humangamestats.model.LocationData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Service for handling location-related operations.
 * Uses FusedLocationProviderClient for efficient location retrieval.
 */
@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {
    
    /**
     * Check if location permissions are granted.
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Get the current location.
     * Returns null if location permission is not granted or location is unavailable.
     */
    suspend fun getCurrentLocation(): LocationData? {
        if (!hasLocationPermission()) {
            return null
        }
        
        return try {
            val cancellationToken = CancellationTokenSource()
            
            suspendCancellableCoroutine { continuation ->
                try {
                    fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                        cancellationToken.token
                    ).addOnSuccessListener { location ->
                        if (location != null) {
                            val locationData = LocationData(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                locationName = getLocationName(location.latitude, location.longitude)
                            )
                            continuation.resume(locationData)
                        } else {
                            continuation.resume(null)
                        }
                    }.addOnFailureListener {
                        continuation.resume(null)
                    }
                } catch (e: SecurityException) {
                    continuation.resume(null)
                }
                
                continuation.invokeOnCancellation {
                    cancellationToken.cancel()
                }
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Get the last known location (faster but may be less accurate).
     */
    suspend fun getLastKnownLocation(): LocationData? {
        if (!hasLocationPermission()) {
            return null
        }
        
        return try {
            suspendCancellableCoroutine { continuation ->
                try {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location ->
                            if (location != null) {
                                val locationData = LocationData(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    locationName = getLocationName(location.latitude, location.longitude)
                                )
                                continuation.resume(locationData)
                            } else {
                                continuation.resume(null)
                            }
                        }
                        .addOnFailureListener {
                            continuation.resume(null)
                        }
                } catch (e: SecurityException) {
                    continuation.resume(null)
                }
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Reverse geocode coordinates to get a location name/address.
     */
    @Suppress("DEPRECATION")
    private fun getLocationName(latitude: Double, longitude: Double): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // For Android 13+, use the async API
                var result: String? = null
                geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                    result = formatAddress(addresses.firstOrNull())
                }
                result
            } else {
                // For older versions, use the deprecated sync API
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                formatAddress(addresses?.firstOrNull())
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Format an address into a readable string.
     */
    private fun formatAddress(address: Address?): String? {
        if (address == null) return null
        
        return buildString {
            // Try to get a concise location description
            address.subLocality?.let { append(it) }
            
            if (isNotEmpty() && address.locality != null) {
                append(", ")
            }
            
            address.locality?.let { append(it) }
            
            if (isEmpty()) {
                // Fallback to admin area or country
                address.adminArea?.let { append(it) }
                address.countryName?.let {
                    if (isNotEmpty()) append(", ")
                    append(it)
                }
            }
        }.takeIf { it.isNotEmpty() }
    }
    
    /**
     * Format coordinates as a string.
     */
    fun formatCoordinates(latitude: Double, longitude: Double): String {
        return String.format(Locale.US, "%.6f, %.6f", latitude, longitude)
    }
    
    /**
     * Get a Google Maps URL for the given coordinates.
     */
    fun getMapsUrl(latitude: Double, longitude: Double): String {
        return "https://www.google.com/maps?q=$latitude,$longitude"
    }
    
    companion object {
        /**
         * Required permissions for location access.
         */
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}
