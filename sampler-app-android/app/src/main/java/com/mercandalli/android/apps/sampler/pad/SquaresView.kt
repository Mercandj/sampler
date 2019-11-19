package com.mercandalli.android.apps.sampler.pad

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat
import com.mercandalli.android.apps.sampler.R
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * A custom view composed with square buttons.
 */
class SquaresView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var textAddOn: TextAddOn = createDefaultTextAddOn()

    /**
     * The measured width of the [SquaresView].
     */
    private var widthInternal: Int = 0

    /**
     * The measured height of the  [SquaresView].
     */
    private var heightInternal: Int = 0

    /**
     * The width of one square.
     */
    private var widthSquare: Float = 0.toFloat()

    /**
     * The height of one square.
     */
    private var heightSquare: Float = 0.toFloat()

    /**
     * TextIndicators (captions)
     */
    private val squareActiveAccentPaint: Paint
    private val squareInactiveAccentPaint: Paint
    private var myTypeface: Typeface? = null
    private var textSize: Int = 0
    private var squareInactiveAccentColor: Int = 0
    private var squareActiveAccentColor: Int = 0

    /**
     * The four squares
     */
    private val backgroundRect = Rect()

    private val squareBackgroundColor: Int

    private val squareBackgroundPaint: Paint
    private val squareStrokeBackgroundActivePaint: Paint
    private val squareStrokeBackgroundInactivePaint: Paint

    private var squareRadius: Int = 0
    private var squarePadding: Int = 0
    private var squareTextPadding: Int = 0
    private var squareStrokeWidth: Int = 0
    private var progressStrokeWidth: Int = 0

    private val pressedSquares = Array(NB_COLUMN) { BooleanArray(NB_LINE) }
    private var progressBeatGrid = Array(NB_COLUMN) { FloatArray(NB_LINE) }

    /**
     * The [SquaresView.OnSquareChangedListener] to handle selection state.
     */
    private var onSquareChangedListener: OnSquareChangedListener? = null
    private val tempRect = RectF()

    init {
        squareBackgroundColor = ContextCompat.getColor(context, R.color.roll_pad_square_background)
        squareActiveAccentColor = ContextCompat.getColor(context, R.color.primary_color_deck_A)
        squareInactiveAccentColor = ContextCompat.getColor(context, R.color.roll_pad_square_accent_inactive)

        textSize = resources.getDimensionPixelSize(R.dimen.pad_text_size)
        squarePadding = resources.getDimensionPixelSize(R.dimen.pad_square_padding)
        squareTextPadding = resources.getDimensionPixelSize(R.dimen.pad_square_text_padding)
        squareRadius = resources.getDimensionPixelSize(R.dimen.pad_square_radius)
        squareStrokeWidth = resources.getDimensionPixelSize(R.dimen.pad_square_stroke_width)
        progressStrokeWidth = resources.getDimensionPixelSize(R.dimen.beatgrid_progress_stroke_width)

        myTypeface = Typeface.createFromAsset(getContext().assets, "fonts/$DEFAULT_FONT")

        squareBackgroundPaint = Paint()
        squareBackgroundPaint.color = squareBackgroundColor
        squareBackgroundPaint.isAntiAlias = true

        squareStrokeBackgroundInactivePaint = Paint()
        squareStrokeBackgroundInactivePaint.isAntiAlias = true
        squareStrokeBackgroundInactivePaint.strokeWidth = squareStrokeWidth.toFloat()
        squareStrokeBackgroundInactivePaint.style = Paint.Style.STROKE
        squareStrokeBackgroundInactivePaint.color = squareInactiveAccentColor

        squareStrokeBackgroundActivePaint = Paint(squareStrokeBackgroundInactivePaint)
        squareStrokeBackgroundActivePaint.color = squareActiveAccentColor

        // Texts
        squareActiveAccentPaint = Paint()
        squareActiveAccentPaint.color = squareActiveAccentColor
        squareActiveAccentPaint.textAlign = Paint.Align.LEFT
        squareActiveAccentPaint.textSize = textSize.toFloat()
        squareActiveAccentPaint.typeface = myTypeface
        squareActiveAccentPaint.isAntiAlias = true

        squareActiveAccentPaint.strokeWidth = progressStrokeWidth.toFloat()
        squareActiveAccentPaint.strokeCap = Paint.Cap.ROUND

        squareInactiveAccentPaint = Paint(squareActiveAccentPaint)
        squareInactiveAccentPaint.color = squareInactiveAccentColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        widthInternal = measuredWidth - paddingLeft - paddingRight
        heightInternal = measuredHeight - paddingTop - paddingBottom

        backgroundRect.set(
            paddingLeft,
            paddingTop,
            paddingLeft + widthInternal,
            paddingTop + heightInternal
        )

        widthSquare = ((widthInternal - (NB_COLUMN - 1) * squarePadding) / NB_COLUMN).toFloat()
        heightSquare = ((heightInternal - (NB_LINE - 1) * squarePadding) / NB_LINE).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var left: Float
        var top: Float
        var right: Float
        var bottom: Float
        for (column in 0 until NB_COLUMN) {
            left = backgroundRect.left + column * (widthSquare + squarePadding)
            right = left + widthSquare
            for (line in 0 until NB_LINE) {
                top = backgroundRect.top + line * (heightSquare + squarePadding)
                bottom = top + heightSquare
                drawRect(canvas, left, top, right, bottom, column, line)
                drawProgressBar(canvas, left, top, right, column, line, progressBeatGrid)
                canvas.drawText(
                    textAddOn.getText(column, NB_COLUMN, line, NB_LINE),
                    left + squareStrokeWidth.toFloat() + squareTextPadding.toFloat(),
                    bottom - squareStrokeWidth.toFloat() - squareTextPadding.toFloat() - squareInactiveAccentPaint.descent(),
                    if (pressedSquares[column][line]) squareActiveAccentPaint else squareInactiveAccentPaint)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        var eventHandled = false
        when (action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN -> eventHandled = handlePointerDown(event)
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> eventHandled = handlePointerUp(event)
        }
        return eventHandled
    }

    /**
     * Setter for the [SquaresView.OnSquareChangedListener]
     *
     * @param onGateChangedListener : The listener to be registered for this view
     */
    fun setOnSquareChangedListener(onGateChangedListener: OnSquareChangedListener?) {
        onSquareChangedListener = onGateChangedListener
    }

    /**
     * Applies new colors for the [SquaresView]
     *
     * @param accentActiveColor : The accent color when the square activated
     */
    fun setStyle(@ColorInt accentActiveColor: Int) {
        squareActiveAccentColor = accentActiveColor
        squareActiveAccentPaint.color = squareActiveAccentColor
        squareStrokeBackgroundActivePaint.color = squareActiveAccentColor
        invalidate()
    }

    fun setTexts(textAddOn: TextAddOn) {
        this.textAddOn = textAddOn
        invalidate()
    }

    fun setProgressBeatGrid(
        @FloatRange(from = 0.0, to = 1.0) progressBeatGrid: Array<FloatArray>
    ) {
        this.progressBeatGrid = progressBeatGrid
        invalidate()
    }

    private fun drawRect(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        column: Int,
        line: Int
    ) {
        tempRect.set(left, top, right, bottom)
        canvas.drawRoundRect(tempRect, squareRadius.toFloat(), squareRadius.toFloat(), squareBackgroundPaint)
        canvas.drawRoundRect(tempRect, squareRadius.toFloat(), squareRadius.toFloat(),
            if (pressedSquares[column][line])
                squareStrokeBackgroundActivePaint
            else
                squareStrokeBackgroundInactivePaint)
    }

    private fun drawProgressBar(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        column: Int,
        line: Int,
        progress: Array<FloatArray>
    ) {
        val leftPosition = left + squareStrokeWidth.toFloat() + squareTextPadding.toFloat() + progressStrokeWidth / 2f
        val rightPosition = right - squareStrokeWidth.toFloat() - squareTextPadding.toFloat() - progressStrokeWidth / 2f
        val topPosition = top + squareStrokeWidth.toFloat() + squareTextPadding.toFloat() + progressStrokeWidth / 2f

        val width = (rightPosition - leftPosition) / 2
        tempRect.set(leftPosition,
            topPosition,
            leftPosition + width,
            topPosition)
        canvas.drawLine(tempRect.left, tempRect.top, tempRect.right, tempRect.bottom, squareInactiveAccentPaint)

        if (progress[column][line] > 0f) {
            tempRect.right = tempRect.left + tempRect.width() * progress[column][line]
            canvas.drawLine(tempRect.left, tempRect.top, tempRect.right, tempRect.bottom, squareActiveAccentPaint)
        }
    }

    private fun handlePointerDown(event: MotionEvent): Boolean {
        val pointer = Pointer()
        pointer.x = event.x
        pointer.y = event.y
        val selectedButton = intersectSquareSelector(event)
        if (selectedButton != null) {
            pressedSquares[selectedButton.x][selectedButton.y] = true
            pointer.setCurrentButton(selectedButton)
            this.notifyListener(selectedButton, true)
            invalidate()
        }
        return true
    }

    private fun handlePointerUp(event: MotionEvent): Boolean {
        val intersectSquareSelector = intersectSquareSelector(event)
        if (intersectSquareSelector != null) {
            pressedSquares[intersectSquareSelector.x][intersectSquareSelector.y] = false
            this.notifyListener(intersectSquareSelector, false)
        }
        invalidate()
        return true
    }

    private fun intersectSquareSelector(event: MotionEvent): Point? {
        // get pointer index from the event object
        val pointerIndex = event.actionIndex
        val pointerX = event.getX(pointerIndex)
        val pointerY = event.getY(pointerIndex)

        if (backgroundRect.contains(pointerX.toInt(), pointerY.toInt())) {
            val column = (pointerX - backgroundRect.left) / (backgroundRect.width() / NB_COLUMN)
            if (abs(column - column.roundToInt().toFloat()) < squarePadding.toFloat() / widthInternal.toFloat()) {
                return null
            }

            val line = (pointerY - backgroundRect.top) / (backgroundRect.height() / NB_LINE)
            if (abs(line - line.roundToInt().toFloat()) < squarePadding.toFloat() / heightInternal.toFloat()) {
                return null
            }

            if (column < NB_COLUMN && line < NB_LINE) {
                return Point(column.toInt(), line.toInt())
            }
        }

        return null
    }

    private fun notifyListener(coordinateButton: Point, checked: Boolean) {
        if (onSquareChangedListener != null) {
            onSquareChangedListener!!.onSquareCheckedChanged(
                getButtonId(coordinateButton.x, NB_COLUMN, coordinateButton.y, NB_LINE),
                coordinateButton.x,
                coordinateButton.y,
                checked
            )
        }
    }

    private fun getButtonId(
        column: Int,
        columnSize: Int,
        line: Int,
        lineSize: Int
    ): Int {
        return line + column * lineSize
    }

    private fun createDefaultTextAddOn() = object : TextAddOn {
        override fun getText(
            column: Int,
            columnSize: Int,
            line: Int,
            lineSize: Int
        ): String {
            val index = line + column * lineSize
            return "A-$index"
        }
    }

    private inner class Pointer {
        var x: Float = 0.toFloat()
        var y: Float = 0.toFloat()
        internal var currentButton: Point? = null

        fun setCurrentButton(currentButton: Point) {
            this.currentButton = currentButton
        }
    }

    interface OnSquareChangedListener {

        fun onSquareCheckedChanged(
            idButton: Int,
            x: Int,
            y: Int,
            checked: Boolean
        )
    }

    interface TextAddOn {

        fun getText(
            column: Int,
            columnSize: Int,
            line: Int,
            lineSize: Int
        ): String
    }

    companion object {

        internal const val NB_LINE = 4
        internal const val NB_COLUMN = 4

        /**
         * The default typo
         */
        private const val DEFAULT_FONT = "Montserrat-Regular.ttf"
    }
}
