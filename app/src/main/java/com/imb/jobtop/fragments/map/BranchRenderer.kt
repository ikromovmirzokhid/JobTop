package com.imb.jobtop.fragments.map

import android.content.Context
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.imb.jobtop.model.Job

class BranchRenderer(
    clusterManager: ClusterManager<Job>,
    val context: Context?,
    map: GoogleMap
) : DefaultClusterRenderer<Job>(context, map, clusterManager) {

    private val mIconGenerator = IconGenerator(context)
    private var mImageView: ImageView = ImageView(context)

    init {
        mIconGenerator.setContentView(mImageView)
        mIconGenerator.setBackground(null)
    }

    override fun onBeforeClusterItemRendered(item: Job, markerOptions: MarkerOptions) {
//        item.profilePhoto.let { mImageView.setImageResource(it!!) }
//        val icon = mIconGenerator.makeIcon()
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))?.title(item.jobTitle)
    }

}