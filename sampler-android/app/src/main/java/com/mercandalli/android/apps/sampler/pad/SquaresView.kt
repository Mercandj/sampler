package com.mercandalli.android.apps.sampler.pad

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.mercandalli.android.apps.sampler.R

/**
 * A custom view composed with square buttons.
 */
class SquaresView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var texts = TEXT_BUTTONS_A

    /**
     * The measured width of the [SquaresView].
     */
    private var mWidth: Int = 0

    /**
     * The measured height of the  [SquaresView].
     */
    private var mHeight: Int = 0

    /**
     * The width of one square.
     */
    private var mWidthSquare: Float = 0.toFloat()

    /**
     * The height of one square.
     */
    private var mHeightSquare: Float = 0.toFloat()

    /**
     * TextIndicators (captions)
     */
    protected val mSquareActiveAccentPaint: Paint
    protected val mSquareInactiveAccentPaint: Paint
    protected var myTypeface: Typeface? = null
    protected var mTextSize: Int = 0
    protected var mSquareInactiveAccentColor: Int = 0
    protected var mSquareActiveAccentColor: Int = 0

    /**
     * The four squares
     */
    protected val mRectBkg = Rect()

    protected val mSquareBackgroundColor: Int

    protected val mSquareBackgroundPaint: Paint
    protected val mSquareStrokeBackgroundActivePaint: Paint
    protected val mSquareStrokeBackgroundInactivePaint: Paint

    protected var mSquareRadius: Int = 0
    protected var mSquarePadding: Int = 0
    protected var mSquareTextPadding: Int = 0
    protected var mSquareStrokeWidth: Int = 0
    protected var mProgressStrokeWidth: Int = 0

    protected val mPressedSquares: Array<BooleanArray>

    @FloatRange(from = 0.0, to = 1.0)
    private var mPrgressBeatgrid: Float = 0.toFloat()

    /**
     * The [SquaresView.OnSquareChangedListener] to handle selection state.
     */
    private var mOnSquareChangedListener: OnSquareChangedListener? = null
    private val mTempRect = RectF()

    init {
        val res = resources

        mTextSize = res.getDimensionPixelSize(R.dimen.pad_text_size)

        mSquareBackgroundColor = ContextCompat.getColor(context, R.color.roll_pad_square_background)
        mSquareActiveAccentColor = ContextCompat.getColor(context, R.color.primary_color_deck_A)
        mSquareInactiveAccentColor = ContextCompat.getColor(context, R.color.roll_pad_square_accent_inactive)

        mSquarePadding = res.getDimensionPixelSize(R.dimen.pad_square_padding)
        mSquareTextPadding = res.getDimensionPixelSize(R.dimen.pad_square_text_padding)
        mSquareRadius = res.getDimensionPixelSize(R.dimen.pad_square_radius)
        mSquareStrokeWidth = res.getDimensionPixelSize(R.dimen.pad_square_stroke_width)

        mProgressStrokeWidth = res.getDimensionPixelSize(R.dimen.beatgrid_progress_stroke_width)

        myTypeface = Typeface.createFromAsset(getContext().assets, "fonts/$DEFAULT_FONT")

        mPressedSquares = Array(NB_COLUMN) { BooleanArray(NB_LINE) }

        mSquareBackgroundPaint = Paint()
        mSquareBackgroundPaint.color = mSquareBackgroundColor
        mSquareBackgroundPaint.isAntiAlias = true

        mSquareStrokeBackgroundInactivePaint = Paint()
        mSquareStrokeBackgroundInactivePaint.isAntiAlias = true
        mSquareStrokeBackgroundInactivePaint.strokeWidth = mSquareStrokeWidth.toFloat()
        mSquareStrokeBackgroundInactivePaint.style = Paint.Style.STROKE
        mSquareStrokeBackgroundInactivePaint.color = mSquareInactiveAccentColor

        mSquareStrokeBackgroundActivePaint = Paint(mSquareStrokeBackgroundInactivePaint)
        mSquareStrokeBackgroundActivePaint.color = mSquareActiveAccentColor

        // Texts
        mSquareActiveAccentPaint = Paint()
        mSquareActiveAccentPaint.color = mSquareActiveAccentColor
        mSquareActiveAccentPaint.textAlign = Paint.Align.LEFT
        mSquareActiveAccentPaint.textSize = mTextSize.toFloat()
        mSquareActiveAccentPaint.typeface = myTypeface
        mSquareActiveAccentPaint.isAntiAlias = true

        mSquareActiveAccentPaint.strokeWidth = mProgressStrokeWidth.toFloat()
        mSquareActiveAccentPaint.strokeCap = Paint.Cap.ROUND

        mSquareInactiveAccentPaint = Paint(mSquareActiveAccentPaint)
        mSquareInactiveAccentPaint.color = mSquareInactiveAccentColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = measuredWidth - paddingLeft - paddingRight
        mHeight = measuredHeight - paddingTop - paddingBottom

        mRectBkg.set(paddingLeft, paddingTop, paddingLeft + mWidth, paddingTop + mHeight)

        mWidthSquare = ((mWidth - (NB_COLUMN - 1) * mSquarePadding) / NB_COLUMN).toFloat()
        mHeightSquare = ((mHeight - (NB_LINE - 1) * mSquarePadding) / NB_LINE).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var left: Float
        var top: Float
        var right: Float
        var bottom: Float
        for (column in 0 until NB_COLUMN) {
            left = mRectBkg.left + column * (mWidthSquare + mSquarePadding)
            right = left + mWidthSquare
            for (line in 0 until NB_LINE) {
                top = mRectBkg.top + line * (mHeightSquare + mSquarePadding)
                bottom = top + mHeightSquare
                drawRect(canvas, left, top, right, bottom, column, line)
                drawProgressBar(canvas, left, top, right, column, line, mPrgressBeatgrid)
                canvas.drawText(texts[column][line],
                        left + mSquareStrokeWidth.toFloat() + mSquareTextPadding.toFloat(),
                        bottom - mSquareStrokeWidth.toFloat() - mSquareTextPadding.toFloat() - mSquareInactiveAccentPaint.descent(),
                        if (mPressedSquares[column][line]) mSquareActiveAccentPaint else mSquareInactiveAccentPaint)
            }
        }
    }

    protected fun drawRect(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, column: Int, line: Int) {
        mTempRect.set(left, top, right, bottom)
        canvas.drawRoundRect(mTempRect, mSquareRadius.toFloat(), mSquareRadius.toFloat(), mSquareBackgroundPaint)
        canvas.drawRoundRect(mTempRect, mSquareRadius.toFloat(), mSquareRadius.toFloat(),
                if (mPressedSquares[column][line])
                    mSquareStrokeBackgroundActivePaint
                else
                    mSquareStrokeBackgroundInactivePaint)
    }

    protected fun drawProgressBar(canvas: Canvas, left: Float, top: Float, right: Float, column: Int, line: Int, progress: Float) {
        val leftPosition = left + mSquareStrokeWidth.toFloat() + mSquareTextPadding.toFloat() + mProgressStrokeWidth / 2f
        val rightPosition = right - mSquareStrokeWidth.toFloat() - mSquareTextPadding.toFloat() - mProgressStrokeWidth / 2f
        val topPosition = top + mSquareStrokeWidth.toFloat() + mSquareTextPadding.toFloat() + mProgressStrokeWidth / 2f

        val width = (rightPosition - leftPosition) / 2
        mTempRect.set(leftPosition,
                topPosition,
                leftPosition + width,
                topPosition)
        canvas.drawLine(mTempRect.left, mTempRect.top, mTempRect.right, mTempRect.bottom, mSquareInactiveAccentPaint)

        if (mPressedSquares[column][line]) {
            mTempRect.right = mTempRect.left + mTempRect.width() * progress
            canvas.drawLine(mTempRect.left, mTempRect.top, mTempRect.right, mTempRect.bottom, mSquareActiveAccentPaint)
        }
    }

    /**
     * Setter for the [SquaresView.OnSquareChangedListener]
     *
     * @param onGateChangedListener : The listener to be registered for this view
     */
    fun setOnSquareChangedListener(onGateChangedListener: OnSquareChangedListener?) {
        mOnSquareChangedListener = onGateChangedListener
    }

    /**
     * Applies new colors for the [SquaresView]
     *
     * @param accentActiveColor : The accent color when the square activated
     */
    fun setStyle(@ColorInt accentActiveColor: Int) {
        mSquareActiveAccentColor = accentActiveColor
        mSquareActiveAccentPaint.color = mSquareActiveAccentColor
        mSquareStrokeBackgroundActivePaint.color = mSquareActiveAccentColor
        invalidate()
    }

    fun setTexts(texts: Array<Array<String>>) {
        this.texts = texts
        invalidate()
    }

    fun setPrgressBeatgrid(@FloatRange(from = 0.0, to = 1.0) prgressBeatgrid: Float) {
        mPrgressBeatgrid = prgressBeatgrid
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        var eventHandled = false
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> eventHandled = handlePointerDown(event)
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> eventHandled = handlePointerUp(event)
        }
        return eventHandled
    }

    protected fun handlePointerDown(event: MotionEvent): Boolean {
        val pointer = Pointer()
        pointer.x = event.x
        pointer.y = event.y
        val selectedButton = intersectSquareSelector(event)
        if (selectedButton != null) {
            mPressedSquares[selectedButton.x][selectedButton.y] = true
            pointer.setCurrentButton(selectedButton)
            this.notifyListener(selectedButton, true)
            invalidate()
        }
        return true
    }

    protected fun handlePointerUp(event: MotionEvent): Boolean {
        val intersectSquareSelector = intersectSquareSelector(event)
        if (intersectSquareSelector != null) {
            mPressedSquares[intersectSquareSelector.x][intersectSquareSelector.y] = false
            this.notifyListener(intersectSquareSelector, false)
        }
        invalidate()
        return true
    }

    protected fun intersectSquareSelector(event: MotionEvent): Point? {
        // get pointer index from the event object
        val pointerIndex = event.actionIndex
        val pointerX = event.getX(pointerIndex)
        val pointerY = event.getY(pointerIndex)

        if (mRectBkg.contains(pointerX.toInt(), pointerY.toInt())) {
            val column = (pointerX - mRectBkg.left) / (mRectBkg.width() / NB_COLUMN)
            if (Math.abs(column - Math.round(column).toFloat()) < mSquarePadding.toFloat() / mWidth.toFloat()) {
                return null
            }

            val line = (pointerY - mRectBkg.top) / (mRectBkg.height() / NB_LINE)
            if (Math.abs(line - Math.round(line).toFloat()) < mSquarePadding.toFloat() / mHeight.toFloat()) {
                return null
            }

            if (column < NB_COLUMN && line < NB_LINE) {
                return Point(column.toInt(), line.toInt())
            }
        }

        return null
    }

    /**
     * Notify the listner about a change in the square selected
     */
    private fun notifyListener(coordinateButton: Point, isChecked: Boolean) {
        if (mOnSquareChangedListener != null) {
            mOnSquareChangedListener!!.onSquareCheckedChanged(BEAT_GRID_VALUES[coordinateButton.x][coordinateButton.y], isChecked)
        }
    }

    protected inner class Pointer {
        var x: Float = 0.toFloat()
        var y: Float = 0.toFloat()
        internal var currentButton: Point? = null

        fun setCurrentButton(currentButton: Point) {
            this.currentButton = currentButton
        }
    }

    /**
     * The interface notifying a change in the square selected
     */
    interface OnSquareChangedListener {
        /**
         * This method notifies when the gate selection started.
         *
         * @param idButton : the id of the beat grid schema.
         */
        fun onSquareCheckedChanged(idButton: Int, isChecked: Boolean)
    }

    companion object {

        val BeatGridPreset_A = 0
        val BeatGridPreset_B = 1
        val BeatGridPreset_C = 2
        val BeatGridPreset_D = 3
        val BeatGridPreset_E = 4
        val BeatGridPreset_F = 5
        val BeatGridPreset_G = 6
        val BeatGridPreset_H = 7
        val BeatGridPreset_I = 8
        val BeatGridPreset_J = 9
        val BeatGridPreset_K = 10
        val BeatGridPreset_L = 11

        internal val NB_LINE = 4
        internal val NB_COLUMN = 3

        /**
         * The string values for the BEATGRID FX
         */
         val TEXT_BUTTONS_A = arrayOf(arrayOf("A-0", "A-3", "A-6", "A-9"), arrayOf("A-1", "A-4", "A-7", "A-10"), arrayOf("A-2", "A-5", "A-8", "A-11"))

        /**
         * The string values for the BEATGRID FX
         */
         val TEXT_BUTTONS_B = arrayOf(arrayOf("B-0", "B-3", "B-6", "B-9"), arrayOf("B-1", "B-4", "B-7", "B-10"), arrayOf("B-2", "B-5", "B-8", "B-11"))

        /**
         * The id values for the BEATGRID FX
         */
        private val BEAT_GRID_VALUES = arrayOf(intArrayOf(BeatGridPreset_A, BeatGridPreset_D, BeatGridPreset_G, BeatGridPreset_J), intArrayOf(BeatGridPreset_B, BeatGridPreset_E, BeatGridPreset_H, BeatGridPreset_K), intArrayOf(BeatGridPreset_C, BeatGridPreset_F, BeatGridPreset_I, BeatGridPreset_L))

        /**
         * The default typo
         */
        private val DEFAULT_FONT = "Montserrat-Regular.ttf"
    }
}
