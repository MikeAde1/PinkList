package com.example.mike.pinklist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mike.pinklist.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet.*

class BottomFragment(): BottomSheetDialogFragment() {

    private lateinit var itemClickListener: ItemClickListener

    companion object {
        fun newInstance(): BottomFragment {
            return BottomFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onClick: ItemClickListener = activity?.supportFragmentManager?.findFragmentByTag("tag") as ItemClickListener
        tv_gallery.setOnClickListener {
            dismiss()
            onClick.onItemClick(GALLERY)
        }
        tv_camera.setOnClickListener {
            dismiss()
            onClick.onItemClick(CAMERA)
        }
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}

const val CAMERA = 1
const val GALLERY = 0