package com.example.tryingmaps

import com.google.android.gms.maps.SupportMapFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {

    lateinit var  mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapFragment=supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap=it

        val myloc = LatLng(33.64431193904693, 73.16495497308165)
        googleMap.addMarker(MarkerOptions()
                    .position(myloc)
                    .title("MyLoc"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myloc,15f))
        })
    }


//    override fun onMapClick(point: LatLng) {
//        Toast.makeText(this, "myLoc", Toast.LENGTH_LONG).show()
//        googleMap.addMarker(MarkerOptions()
//                        .position(point)
//                        .title("Location"))
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point,15f))
//    }

}


