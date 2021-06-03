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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.sdp13epfl2021.projmag.R
import com.sdp13epfl2021.projmag.activities.ProjectCreationActivity.Companion.CREATION_STRING
import com.sdp13epfl2021.projmag.activities.ProjectCreationActivity.Companion.EDIT_EXTRA
import com.sdp13epfl2021.projmag.activities.ProjectsListActivity.Companion.PROJECT_EXTRA
import com.sdp13epfl2021.projmag.databinding.ActivityMapsBinding
import com.sdp13epfl2021.projmag.model.ImmutableProject
import com.sdp13epfl2021.projmag.model.ImmutableProject.Companion.FieldNames.LATITUDE
import com.sdp13epfl2021.projmag.model.ImmutableProject.Companion.FieldNames.LONGITUDE

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private val EPFL = LatLng(46.51886897414146, 6.566790856808788)
        private const val INITIAL_ZOOM = 15.5f
        const val LOCATION_EXTRA = "location"
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var newMarker: Marker? = null
    private var latitude: Double = Double.MAX_VALUE
    private var longitude: Double = Double.MAX_VALUE
    private lateinit var project: ImmutableProject
    private var nextActivityString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nextActivityString = intent.getStringExtra(LOCATION_EXTRA)

        project = intent.getParcelableExtra(PROJECT_EXTRA)!!
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(EPFL, INITIAL_ZOOM))

        if (project.latitude != null && project.longitude != null) {
            newMarker = mMap.addMarker(
                MarkerOptions().position(LatLng(project.latitude!!, project.longitude!!))
                    .title(project.name)
            )
        }

        if (nextActivityString == CREATION_STRING) {
            mMap.setOnMapClickListener { latLng ->
                newMarker?.let { it.remove() }
                newMarker = mMap.addMarker(
                    MarkerOptions().position(latLng).title(
                        project.name
                    )
                )
                latitude = latLng.latitude
                longitude = latLng.longitude
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maps_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val nextActivity =
            if (nextActivityString == CREATION_STRING)
                ProjectCreationActivity::class.java
            else
                ProjectInformationActivity::class.java
        val newIntent = Intent(this, nextActivity)
        newIntent.putExtra(PROJECT_EXTRA, project)
        if (nextActivityString == CREATION_STRING) {
            newIntent.putExtra(LOCATION_EXTRA, true)
            newIntent.putExtra(EDIT_EXTRA, project)
            newIntent.putExtra(LATITUDE, latitude)
            newIntent.putExtra(LONGITUDE, longitude)
        }
        startActivity(newIntent)
        finish()
        return super.onOptionsItemSelected(item)
    }
}