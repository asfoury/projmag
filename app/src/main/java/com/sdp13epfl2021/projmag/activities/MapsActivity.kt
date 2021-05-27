package com.sdp13epfl2021.projmag.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.activities.ProjectCreationActivity.Companion.CREATION_STRING
import com.sdp13epfl2021.projmag.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val epfl = LatLng(46.51886897414146, 6.566790856808788)
        //mMap.addMarker(MarkerOptions().position(epfl).title("Marker at EPFL"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(epfl, 15.5f))
        mMap.setOnMapClickListener { latLng ->
            val newMarker = mMap.addMarker(MarkerOptions().position(latLng).title("New Marker"))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maps_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mapsDoneButton) {
            val returnToActivity = intent.getStringExtra(ProjectCreationActivity.LOCATION_EXTRA)
            val nextActivity =
                if (returnToActivity == CREATION_STRING)
                    ProjectCreationActivity::class.java
                else
                    ProjectInformationActivity::class.java
            val newIntent = Intent(this, nextActivity)
            startActivity(newIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}