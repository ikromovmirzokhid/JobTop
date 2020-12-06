package com.imb.jobtop.fragments.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import com.imb.jobtop.R
import com.imb.jobtop.fragments.base.BaseFragment
import com.imb.jobtop.model.Job
import com.imb.jobtop.model.Jobs
import com.imb.jobtop.utils.PermissionHandler
import com.imb.jobtop.utils.extensions.getData
import com.imb.jobtop.utils.extensions.putData
import com.nabinbhandari.android.permissions.Permissions
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class FragmentMapJobs : BaseFragment(R.layout.fragment_map_jobs), OnMapReadyCallback {

    private var mMapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null
    private var mClusterManager: ClusterManager<Job>? = null
    private var clusterItems: MutableList<Job>? = mutableListOf()

    private var jobs: MutableList<Job>? = null
    private var lastLatLng = LatLng(41.2995, 69.2401)
    private var isGranted = false
    private var myPersonalLocation = LatLng(0.0, 0.0)
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    private var mapAlreadyCreated = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!mapAlreadyCreated)
            jobs?.clear()

        jobs = arguments?.getData<Jobs>("data_list")!!.list

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        mMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mMapFragment?.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap?) {
        println("Debugging123 - onMapReady")
        mapAlreadyCreated = true
        googleMap = map
        openDefaultLocation()
        initClusterManager()
        showMyLocation()
    }

    private fun openDefaultLocation() {
        println("Debugging123 - openDefaultLocation")

        val tashkentLocation = LatLng(41.2995, 69.2401)

        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(tashkentLocation, 15f))
        googleMap?.animateCamera(CameraUpdateFactory.zoomIn())
        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(10f), 2000, null)
        val cameraPosition = CameraPosition.Builder()
            .target(tashkentLocation)      // Sets the center of the map to Mountain View
            .zoom(5f)                   // Sets the zoom
            .bearing(0f)                // Sets the orientation of the camera to east
            .tilt(30f)                   // Sets the tilt of the camera to 30 degrees
            .build()                   // Creates a CameraPosition from the builder
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun initClusterManager() {
        println("Debugging123 - initClusterManager")

        mClusterManager = ClusterManager(context, googleMap)
        mClusterManager?.let { c ->
            googleMap?.let {
                mClusterManager?.renderer = BranchRenderer(c, context, it)
            }
        }
        mClusterManager?.setOnClusterItemClickListener { item ->
            moveCamera(item)
            val bundle = Bundle()
            bundle.putData("job", item)
            findNavController().navigate(R.id.jobDetailFragment, bundle)
            true
        }

        mClusterManager?.setOnClusterClickListener {
            val builder = LatLngBounds.builder()
            for (item in it.items) {
                builder.include(item.position)
            }
            val bounds = builder.build()
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200))
            true
        }

        googleMap?.setOnCameraIdleListener(mClusterManager)
        googleMap?.setOnMarkerClickListener(mClusterManager)
        googleMap?.setOnInfoWindowClickListener(mClusterManager)
    }

    private fun moveCamera(it: Job) {
        googleMap?.animateCamera(CameraUpdateFactory.zoomIn())
        googleMap?.animateCamera(CameraUpdateFactory.zoomTo(10f), 2000, null)
        val newLatLng = LatLng(it.position.latitude - 0.00016, it.position.longitude)
        val cameraPosition = CameraPosition.Builder()
            .target(newLatLng)      // Sets the center of the map to Mountain View
            .zoom(20f)                   // Sets the zoom
            .bearing(0f)                // Sets the orientation of the camera to east
            .tilt(30f)                   // Sets the tilt of the camera to 30 degrees
            .build()                   // Creates a CameraPosition from the builder
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        lastLatLng = it.position
    }

    @SuppressLint("MissingPermission")
    private fun showMyLocation() {
        println("Debugging123 - showMyLocation")
        println("Debugging123 - isMyLocationEnabled $isGranted")

        googleMap?.isMyLocationEnabled = true
//        googleMap?.
        googleMap?.uiSettings?.isMyLocationButtonEnabled = true

        checkFineLocationPermission()

        googleMap?.setOnMyLocationButtonClickListener {
            checkFineLocationPermission()
            true
        }
    }

    private fun checkFineLocationPermission() {
        println("Debugging123 - checkFineLocationPermission")
        val UPDATE_INTERVAL = 15_000
        val UPDATE_FASTEST_INTERVAL = 5_000
        val UPDATE_MIN_DISTANCE = 20
        val handler = PermissionHandler()
        handler.onGranted {
            isGranted = true
            val locationRequest = LocationRequest()
                .setInterval(UPDATE_INTERVAL.toLong())
                .setFastestInterval(UPDATE_FASTEST_INTERVAL.toLong())
                .setSmallestDisplacement(UPDATE_MIN_DISTANCE.toFloat())
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            try {
                mFusedLocationClient?.requestLocationUpdates(
                    locationRequest,
                    mLocationCallback,
                    null
                )
            } catch (e: SecurityException) {
                Log.d("Debugging123", "$e")
            }
//            showMyLocation()
        }
        Permissions.check(context, Manifest.permission.ACCESS_FINE_LOCATION, null, handler)
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
//            val t = sortBranchesByDistance(locationResult, data)
//            jobs?.clear()
//            data.addAll(t)
            Log.d("TTT", "onLocationResult")
            myPersonalLocation = LatLng(
                locationResult?.lastLocation?.latitude!!,
                locationResult.lastLocation.longitude
            )
            Log.d("Debugging123", "$myPersonalLocation")
            centerIncidentRouteOnMap(getCirclePolygonCoords(myPersonalLocation, 1000))
            replaceData(jobs)
        }
    }

    private fun replaceData(d: MutableList<Job>?) {
        Log.d("TTT", "replaceData")
        d?.toMutableList()?.let { populateMarkers(it) }
    }

    private fun populateMarkers(jobs: MutableList<Job>) {
        mClusterManager?.clearItems()
        clusterItems?.clear()
        googleMap?.clear()

        for (job in jobs) {
            clusterItems?.add(job)
            mClusterManager?.addItems(clusterItems)
        }
        mClusterManager?.cluster()

    }

    private fun centerIncidentRouteOnMap(copiedPoints: ArrayList<LatLng>) {
        var minLat: Double = (Integer.MAX_VALUE).toDouble()
        var maxLat = (Integer.MIN_VALUE).toDouble()
        var minLon = (Integer.MAX_VALUE).toDouble()
        var maxLon = (Integer.MIN_VALUE).toDouble()
        for (point in copiedPoints) {
            maxLat = Math.max(point.latitude, maxLat)
            minLat = Math.min(point.latitude, minLat)
            maxLon = Math.max(point.longitude, maxLon)
            minLon = Math.min(point.longitude, minLon)
        }

        var padding = 0
        activity?.let {
            padding = (getScreenWidth(it) * 0.3).toInt()
        }


        val bounds =
            LatLngBounds.Builder().include(LatLng(maxLat, maxLon)).include(LatLng(minLat, minLon))
                .build()

        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                getScreenWidth(requireActivity()),
                getScreenHeight(requireActivity()),
                padding
            )
        )
    }

    private fun getCirclePolygonCoords(center: LatLng, radius: Int): ArrayList<LatLng> {
        val degreesBetweenPoints = 4.0
        val numberOfPoints = (360 / degreesBetweenPoints).toInt()
        val distRadians: Double = radius / 6371000.0
        val pi: Double = 3.141592653589793238462643
        val centerLatRadians: Double = center.latitude * pi / 180
        val centerLonRadians: Double = center.longitude * pi / 180
        val coordinates = ArrayList<LatLng>()
        for (i in 0 until numberOfPoints) {
            val degrees: Double = i * degreesBetweenPoints
            val degreeRadians: Double = degrees * pi / 180
            val pointLatRadians: Double = asin(
                sin(centerLatRadians) * cos(distRadians) + cos(centerLatRadians) * sin(distRadians) * cos(
                    degreeRadians
                )
            )
            val pointLonRadians: Double = centerLonRadians + atan2(
                sin(degreeRadians) * sin(distRadians) * cos(centerLatRadians),
                cos(distRadians) - sin(centerLatRadians) * sin(pointLatRadians)
            )
            val pointLat: Double = pointLatRadians * 180 / pi
            val pointLon: Double = pointLonRadians * 180 / pi
            val point = LatLng(pointLat, pointLon)
            coordinates.add(point)
        }

        coordinates.add(coordinates.first())

        return coordinates
    }

    private fun getScreenWidth(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x
    }

    private fun getScreenHeight(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y
    }
}