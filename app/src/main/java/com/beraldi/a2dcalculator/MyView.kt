package com.beraldi.a2dcalculator

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast


class MyView(context: Context?) : View(context), View.OnTouchListener {
    
    var dx = 0f //Distance among vertical lines
    var dy = 0f //Distance among horizontal lines



    var op1 = "" //operand as a string
    var op = 0 //operand as number
    var operator = ""

    val vLines = 4f
    val hLines = 6f

    var r : RectF = RectF()

    val mPaint = Paint().apply{
        style=Paint.Style.FILL_AND_STROKE
        color= Color.parseColor("#AA0000AA")
        textSize=100f
    }

    val whitePaint = Paint().apply {
        style=Paint.Style.FILL_AND_STROKE
        color= Color.GREEN
        strokeWidth=2f
        textSize=150f
    }

    init {
        setOnTouchListener(this)

    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        dx=width/vLines
        dy=height/hLines



        if (op1!="") {
            Log.i("LOG", "onTouch: "+op1)
            canvas.drawText(op1,0f,dy/2-mPaint.textSize/2,mPaint)
        }
        r.bottom=dy
        r.top=height.toFloat()
        r.left=0f
        r.right=width.toFloat()
        canvas.drawRect(r,mPaint)

        //Draw  horizontal lines
        for (y in 0..hLines.toInt()-1){
              canvas.drawLine(
                      0f,height-y*dy,
                      width.toFloat(),height-y*dy,
                      whitePaint)
        }
        //Draw vertical lines
        for (x in 0..vLines.toInt()){
            canvas.drawLine(
                    x*dx,dy,
                    x*dx,height.toFloat()-dy,
                    whitePaint)
        }
        var n=0
        var rr= Rect()
        //Place digits from 1 to 9
        for (y in 0..2)
            for (x in 0..2){
                n=3*y+(x+1)
                whitePaint.getTextBounds(n.toString(),0,1,rr)
                val offy = (rr.top-rr.bottom)/2
                val offx = (rr.right-rr.left)/2
                canvas.drawText(n.toString(),
                        x*dx+dx/2-offx,
                        height-(y+1)*dy-dy/2-offy,
                        whitePaint)
            }

        //place 0
        whitePaint.getTextBounds("0",0,1,rr)
        var offy = (rr.top-rr.bottom)/2f
        var offx = (rr.right-rr.left)/2f
        canvas.drawText("0",
                width/2f-offx,
                height-dy/2-offy,
                whitePaint)

        var i = 0
        for ( t in arrayOf("AC","F","/","x")){
            whitePaint.getTextBounds(t,0,t.length,rr)
            offy = (rr.top-rr.bottom)/2f
            offx = (rr.right-rr.left)/2f
            canvas.drawText(t,
                    i*dx+dx/2-offx,
                    height-4*dy-dy/2-offy,
                    whitePaint)
            i++

        }

        i = 1
        for ( t in arrayOf("=","-","+")){
            whitePaint.getTextBounds(t,0,t.length,rr)
            offy = (rr.top-rr.bottom)/2f
            offx = (rr.right-rr.left)/2f
            canvas.drawText(t,
                    3*dx+dx/2-offx,
                    height-i*dy-dy/2-offy,
                    whitePaint)
            i++

        }

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = (event.x / dx).toInt()
                val y = ((height - event.y) / dy).toInt()
                var n = 4 * y + x

                //Toast.makeText(context, "" + n, Toast.LENGTH_SHORT).show()
                when (n) {
                    0, 1, 2, 3 -> {
                        if (op1.length > 0) op1 = op1 + "0"
                    }
                    4, 5, 6 -> {
                        n -= 3;op1 = op1 + n.toString()
                    }
                    8, 9, 10 -> {
                        n -= 4;op1 = op1 + n.toString()
                    }
                    12, 13, 14 -> {
                        n -= 5;op1 = op1 + n.toString()
                    }

                    16 -> {
                        op1 = ""
                    } //AC = All Clear
                    17 -> {
                        Toast.makeText(context, "Sorry, still to implement", Toast.LENGTH_SHORT).show()
                    } //F
                    //TODO: check errors, i.e., no empty arguments are allowed..
                    19 -> {
                        operator = "x";op = op1.toInt();op1 = ""
                    } //X
                    18 -> {
                        operator = "/";op = op1.toInt();op1 = ""
                    } //X
                    15 -> {
                        operator = "+";op = op1.toInt();op1 = ""
                    } //+
                    11 -> {
                        operator = "-";op = op1.toInt();op1 = ""
                    } //-
                    7 -> {
                        when (operator) {
                            "+" -> {
                                op1 = (op + op1.toInt()).toString()
                            }
                            "-" -> {
                                op1 = (op - op1.toInt()).toString()
                            }
                            "/" -> {
                                op1 = (op / op1.toInt()).toString()
                            }
                            "x" -> {
                                op1 = (op * op1.toInt()).toString()
                            }
                        }
                    } //=
                }
                invalidate()

            }
        }
        return true
    }

}