package com.example.currencyrates

import android.annotation.SuppressLint
import android.app.ActionBar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import org.w3c.dom.Text
import java.text.DecimalFormat


class CurrencyActivity : AppCompatActivity() {

    private lateinit var idView: TextView
    private lateinit var forCurr: TextView
    private lateinit var natCurr: TextView
    private lateinit var nativeCurrencyInput: TextView
    private lateinit var foreignCurrencyInput: TextView
    private lateinit var swapButton: Button
    private lateinit var name: String
    private lateinit var numCode: String
    private lateinit var charCode: String
    private lateinit var value: String

    private var oneDecimalPlace = DecimalFormat("#.###")
    private val textWatcher = getTextWatcher()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)

        idView = findViewById(R.id.idNum)
        forCurr = findViewById(R.id.foreignCurrency)
        natCurr = findViewById(R.id.nativeCurrency)
        nativeCurrencyInput = findViewById(R.id.NativeCurrencyInput)
        foreignCurrencyInput = findViewById(R.id.ForeignCurrencyInput)
        swapButton = findViewById(R.id.SwapButton)

        name = intent.getStringExtra("name")!!
        numCode = intent.getStringExtra("numCode")!!
        charCode = intent.getStringExtra("charCode")!!
        value = intent.getStringExtra("value")!!

        oneDecimalPlace = DecimalFormat("#.###")

        supportActionBar?.title = name
        nativeCurrencyInput.text = oneDecimalPlace.format(value.toDoubleOrNull()).toString()
        idView.text = "Код валюты: $numCode"
        forCurr.text = charCode

        nativeCurrencyInput.addTextChangedListener(textWatcher)
        foreignCurrencyInput.addTextChangedListener(textWatcher)

        swapButton.setOnClickListener {
            swapButtonAnimation()
        }
    }

    private fun getTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (nativeCurrencyInput.isFocused) {
                    foreignCurrencyInput.removeTextChangedListener(this)
                    if (nativeCurrencyInput.text!!.isNotEmpty())
                        foreignCurrencyInput.text =
                            (oneDecimalPlace.format(value.toDoubleOrNull()?.let {
                                nativeCurrencyInput.text.toString().toDoubleOrNull()
                                    ?.div(it)
                            })).toString()
                    else {
                        foreignCurrencyInput.text = "0.0"
                    }
                    foreignCurrencyInput.addTextChangedListener(this)
                } else if (foreignCurrencyInput.isFocused) {
                    nativeCurrencyInput.removeTextChangedListener(this)
                    if (foreignCurrencyInput.text!!.isNotEmpty())
                        nativeCurrencyInput.text =
                            oneDecimalPlace.format((value.toDoubleOrNull()?.let {
                                foreignCurrencyInput.text.toString().toDoubleOrNull()
                                    ?.times(it)
                            })).toString()
                    else {
                        nativeCurrencyInput.text = "0.0"
                    }
                    nativeCurrencyInput.addTextChangedListener(this)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //
            }
        }
    }

    private fun swapButtonAnimation() {
        swapButton.animate().apply {
            duration = 1000
            rotation(450f)
        }.withEndAction {
            swapButton.rotation = 90f
        }

        idView.animate().apply {
            duration = 1000
            scaleX(2f)
            scaleY(2f)
        }.withEndAction {
            idView.animate().apply {
                duration = 1000
                scaleX(1f)
                scaleY(1f)
            }.start()
        }

        nativeCurrencyInput.animate().apply {
            duration = 1000
            scaleY(0.1f)
            scaleX(0.1f)
            rotation(360f)
        }.withEndAction {
            nativeCurrencyInput.animate().apply {
                duration = 1000
                scaleX(1f)
                scaleY(1f)
                rotation(0f)
            }
        }

        foreignCurrencyInput.animate().apply {
            duration = 1000
            scaleY(0.1f)
            scaleX(0.1f)
            rotation(360f)
        }.withEndAction {
            foreignCurrencyInput.animate().apply {
                duration = 1000
                scaleX(1f)
                scaleY(1f)
                rotation(0f)
            }
        }

        forCurr.animate().apply {
            duration = 1000
            scaleY(0.1f)
            scaleX(0.1f)
        }.withEndAction {
            forCurr.animate().apply {
                duration = 1000
                scaleX(1f)
                scaleY(1f)
            }
        }
        natCurr.animate().apply {
            duration = 1000
            scaleY(0.1f)
            scaleX(0.1f)
        }.withEndAction {
            natCurr.animate().apply {
                duration = 1000
                scaleX(1f)
                scaleY(1f)
            }
        }
    }
}
