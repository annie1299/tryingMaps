package com.example.tryingmaps

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var currentLocation: Location? = null

    val arrayList1 = mutableListOf<LatLng>()
    val markers = mutableListOf<Marker>()
    var polyline1: Polyline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
            return
        }
        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { location ->
            if (location != null) {
                this.currentLocation = location
                mapFragment =
                    supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    override fun onMapReady(gm: GoogleMap) {
        googleMap = gm
//        pointa1 = currentLocation?.latitude!!
//        pointa2 = currentLocation?.longitude!!
//        val LatLng = LatLng(pointa1, pointa2)
//        drawMarker(LatLng)
//        arrayList1.add(LatLng)
        googleMap.setOnMapClickListener(this)
    }

    override fun onMapClick(point: LatLng) {
        drawMarker(point)
    }

    private fun drawMarker(point: LatLng) {
        if (arrayList1.size == 0) {
            for (item in markers) {
                item.remove()
            }
            markers.clear()
        }
        arrayList1.add(point)
        Toast.makeText(this, "Location ${arrayList1.size}", Toast.LENGTH_LONG).show()
        val currentMarker = googleMap.addMarker(
            MarkerOptions().position(point).title("Location")
                .snippet(getAddress(point.latitude, point.longitude)).draggable(true)
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(point))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15f))
        markers.add(currentMarker)

        if (polyline1 != null) {
            polyline1?.remove()
        }
        if (arrayList1.size == 2) {
            addPolyLine()
            arrayList1.clear()
        }
    }

    private fun addPolyLine() {
        // Toast.makeText(this, "${arrayList1.get(0).longitude},${arrayList1.get(0).longitude}", Toast.LENGTH_LONG).show()
        polyline1 = googleMap.addPolyline(
            PolylineOptions()
                .add(
                    LatLng(arrayList1.get(0).latitude, arrayList1.get(0).longitude),
                    LatLng(arrayList1.get(1).latitude, arrayList1.get(1).longitude)
                )
                .width(5f)
                .color(Color.RED)
                .geodesic(true)
        )
        return
    }

    private fun getAddress(lat: Double, lon: Double): String? {
        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, lon, 1)
        return address[0].getAddressLine(0).toString()
    }

    private fun getDirectionURL() {

    }

}