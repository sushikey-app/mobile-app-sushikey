package com.am.projectinternalresto.utils

import android.graphics.Canvas
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class CustomStyleRoundedBarChart(
    chart: com.github.mikephil.charting.charts.BarChart,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)
        val buffer = mBarBuffers[index]
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        val isSingleColor = dataSet.colors.size == 1

        if (isSingleColor) {
            mRenderPaint.color = dataSet.color
        }

        var j = 0
        while (j < buffer.size()) {
            val left = buffer.buffer[j]
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2]
            val bottom = buffer.buffer[j + 3]

            if (!mViewPortHandler.isInBoundsLeft(right)) {
                j += 4
                continue
            }

            if (!mViewPortHandler.isInBoundsRight(left)) {
                break
            }

            if (!isSingleColor) {
                mRenderPaint.color = dataSet.getColor(j / 4)
            }

            val radius = 20f // set corner radius

            c.drawRoundRect(RectF(left, top, right, bottom), radius, radius, mRenderPaint)
            if (dataSet.barBorderWidth > 0) {
                mBarBorderPaint.strokeWidth = dataSet.barBorderWidth
                mBarBorderPaint.color = dataSet.barBorderColor
                c.drawRoundRect(RectF(left, top, right, bottom), radius, radius, mBarBorderPaint)
            }

            j += 4
        }
    }
}
