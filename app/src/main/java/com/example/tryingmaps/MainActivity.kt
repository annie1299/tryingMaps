package com.example.tryingmaps

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.maps.SupportMapFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    lateinit var  mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    var currentMarker: Marker? = null

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1000)
            return
        }
        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location ->
            if (location != null) {
                this.currentLocation = location
                mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        when (requestCode) {
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    override fun onMapReady(gm: GoogleMap) {
        googleMap = gm ?: return
        val LatLng = LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)
        drawMarker(LatLng)
        googleMap.setOnMapClickListener(this)
    }

    override fun onMapClick(point: LatLng) {
        if (currentMarker != null) {
            currentMarker?.remove()
            currentMarker = null;
        }
        drawMarker(point)
    }

    private fun drawMarker(point: LatLng) {
        Toast.makeText(this, "myLoc", Toast.LENGTH_LONG).show()
        currentMarker = googleMap.addMarker(MarkerOptions().position(point).title("Location")
                .snippet(getAddress(point.latitude, point.longitude)).draggable(true))
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(point))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15f))
    }

    private fun getAddress(lat: Double, lon: Double): String? {
        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, lon, 1)
        return address[0].getAddressLine(0).toString()
    }
}