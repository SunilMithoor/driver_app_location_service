package com.app.ui.base

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.appcompat.widget.AppCompatImageView


class RotateZoomImageView : AppCompatImageView {
    private var mScaleDetector: ScaleGestureDetector? = null
    private var mImageMatrix: Matrix? = null

    /* Last Rotation Angle */
    private var mLastAngle = 0

    /* Pivot Point for Transforms */
    private var mPivotX = 0
    private var mPivotY = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    private fun init(context: Context) {
        mScaleDetector = ScaleGestureDetector(context, mScaleListener)
        //setScaleType(ScaleType.FIT_XY)
        mImageMatrix = Matrix()
    }

    /*
     * Use onSizeChanged() to calculate values based on the view's size.
     * The view has no size during init(), so we must wait for this
     * callback.
     */
    protected override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            //Shift the image to the center of the view
            val translateX: Int = (w - getDrawable().getIntrinsicWidth()) / 2
            val translateY: Int = (h - getDrawable().getIntrinsicHeight()) / 2
            mImageMatrix?.setTranslate(translateX.toFloat(), translateY.toFloat())
            imageMatrix = mImageMatrix
            //Get the center point for future scale and rotate transforms
            mPivotX = w / 2
            mPivotY = h / 2
        }
    }

    private val mScaleListener: SimpleOnScaleGestureListener =
        object : SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                // ScaleGestureDetector calculates a scale factor based on whether
                // the fingers are moving apart or together
                val scaleFactor = detector.scaleFactor
                //Pass that factor to a scale for the image
                mImageMatrix?.postScale(
                    scaleFactor,
                    scaleFactor,
                    mPivotX.toFloat(),
                    mPivotY.toFloat()
                )
                imageMatrix = mImageMatrix
                return true
            }
        }

    /*
     * Operate on two-finger events to rotate the image.
     * This method calculates the change in angle between the
     * pointers and rotates the image accordingly.  As the user
     * rotates their fingers, the image will follow.
     */
    private fun doRotationEvent(event: MotionEvent): Boolean {
        //Calculate the angle between the two fingers
        val deltaX = event.getX(0) - event.getX(1)
        val deltaY = event.getY(0) - event.getY(1)
        val radians = Math.atan(deltaY / deltaX.toDouble())
        //Convert to degrees
        val degrees = (radians * 180 / Math.PI).toInt()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP ->                 //Mark the initial angle
                mLastAngle = degrees
            MotionEvent.ACTION_MOVE -> {
                // ATAN returns a converted value between -90deg and +90deg
                // which creates a point when two fingers are vertical where the
                // angle flips sign.  We handle this case by rotating a small amount
                // (5 degrees) in the direction we were traveling
                if (degrees - mLastAngle > 45) {
                    //Going CCW across the boundary
                    mImageMatrix?.postRotate(-5f, mPivotX.toFloat(), mPivotY.toFloat())
                } else if (degrees - mLastAngle < -45) {
                    //Going CW across the boundary
                    mImageMatrix?.postRotate(5f, mPivotX.toFloat(), mPivotY.toFloat())
                } else {
                    //Normal rotation, rotate the difference
                    mImageMatrix?.postRotate(
                        (degrees - mLastAngle).toFloat(),
                        mPivotX.toFloat(),
                        mPivotY.toFloat()
                    )
                }
                //Post the rotation to the image
                setImageMatrix(mImageMatrix)
                //Save the current angle
                mLastAngle = degrees
            }
        }
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (event.action == MotionEvent.ACTION_DOWN) {
            // We don't care about this event directly, but we declare
            // interest so we can get later multi-touch events.
            true
        } else when (event.pointerCount) {
            3 ->                 // With three fingers down, zoom the image
                // using the ScaleGestureDetector
                mScaleDetector!!.onTouchEvent(event)
            2 ->                 // With two fingers down, rotate the image
                // following the fingers
                doRotationEvent(event)
            else ->                 //Ignore this event
                super.onTouchEvent(event)
        }
    }
}