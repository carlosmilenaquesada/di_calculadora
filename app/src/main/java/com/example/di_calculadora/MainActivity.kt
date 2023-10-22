package com.example.di_calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import kotlin.math.pow
import kotlin.math.sqrt
import com.example.di_calculadora.ui.theme.Di_calculadoraTheme
import com.example.di_calculadora.ui.theme.digitaldisplay
import com.example.di_calculadora.ui.theme.droidsansmono

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Di_calculadoraTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Pantalla()
                }
            }
        }
    }
}

enum class OperandoActivo {
    PRIMER_OPERANDO, ESTADO_INTERMEDIO, SEGUNDO_OPERANDO
}

enum class OperacionActual {
    NINGUNA, SUMA, RESTA, MULTIPLICA, DIVIDE, PORCIENTO, RAIZ, MODULO, POTENCIA
}


fun sumar(numUno: Double, numDos: Double): Double {
    return numUno + numDos
}

fun restar(numUno: Double, numDos: Double): Double {
    return numUno - numDos
}

fun multiplicar(numUno: Double, numDos: Double): Double {
    return numUno * numDos
}

fun dividir(numUno: Double, numDos: Double): Double {
    return numUno / numDos
}

fun porciento(numUno: Double, numDos: Double): Double {
    return (numUno * numDos) / 100f
}

fun raizCuadrada(num: Double): Double {
    return sqrt(num)
}

fun modulo(numUno: Double, numDos: Double): Double {
    return numUno % numDos
}

fun potencia(numUno: Double, numDos: Double): Double {
    return numUno.pow(numDos)
}

fun truncarDecimalesCero(numero: Double): String {
    var aux: String = numero.toString().take(15)
    if (aux.contains(".")) {

        var terminar = false
        var i = aux.length - 1
        while (i >= 0 && !terminar) {
            if (aux[i] == '.') {
                aux = aux.take(i)
                terminar = true
            } else {
                if (aux[i] == '0') {
                    aux = aux.take(i)
                } else {
                    terminar = true
                }
            }
            i--
        }
    }
    return aux
}


@Preview(showSystemUi = true)
@Composable
fun Pantalla() {


    var primerOperando: Double by rememberSaveable {
        mutableStateOf(0.0)
    }

    var segundoOperando: Double by rememberSaveable {
        mutableStateOf(0.0)
    }

    var resultadoActual: Double by rememberSaveable {
        mutableStateOf(0.0)
    }

    var valorMostrado: String by rememberSaveable {
        mutableStateOf("0")
    }

    var focoActivo: OperandoActivo by rememberSaveable {
        mutableStateOf(OperandoActivo.PRIMER_OPERANDO)
    }

    var operacionActual: OperacionActual by rememberSaveable {
        mutableStateOf(OperacionActual.NINGUNA)
    }

    var mostrarAyuda: Boolean by rememberSaveable {
        mutableStateOf(false)
    }


    fun operar() {
        when (operacionActual) {
            OperacionActual.SUMA -> {
                resultadoActual = sumar(primerOperando, segundoOperando)
            }

            OperacionActual.RESTA -> {
                resultadoActual = restar(primerOperando, segundoOperando)
            }

            OperacionActual.MULTIPLICA -> {
                resultadoActual = multiplicar(primerOperando, segundoOperando)
            }

            OperacionActual.DIVIDE -> {
                resultadoActual = dividir(primerOperando, segundoOperando)
            }

            OperacionActual.PORCIENTO -> {
                resultadoActual = porciento(primerOperando, segundoOperando)
            }

            OperacionActual.RAIZ -> {
                resultadoActual = raizCuadrada(resultadoActual)
            }

            OperacionActual.MODULO -> {
                resultadoActual = modulo(primerOperando, segundoOperando)
            }

            OperacionActual.POTENCIA -> {
                resultadoActual = potencia(primerOperando, segundoOperando)
            }

            else -> {}
        }
    }

    fun onClickOperacion() {
        if (focoActivo == OperandoActivo.PRIMER_OPERANDO) {
            primerOperando = valorMostrado.toDouble()
            focoActivo = OperandoActivo.ESTADO_INTERMEDIO
        } else {
            segundoOperando = valorMostrado.toDouble()
            operar()
            valorMostrado = truncarDecimalesCero(resultadoActual)
            primerOperando = valorMostrado.toDouble()
            segundoOperando = 0.0
            focoActivo = OperandoActivo.ESTADO_INTERMEDIO
        }
    }

    fun onClickNumero(numero: String) {
        if (focoActivo == OperandoActivo.ESTADO_INTERMEDIO) {
            valorMostrado = "0"
            focoActivo = OperandoActivo.SEGUNDO_OPERANDO
        }
        if (valorMostrado == "0") {
            valorMostrado = numero
        } else {
            if ((valorMostrado.length in 1..14)) {
                valorMostrado += numero
            }
        }
    }

    @Composable
    fun textoBoton(signo: String, fontSize: TextUnit) {
        Text(
            text = signo,
            fontSize = fontSize,
            fontFamily = droidsansmono,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (mostrarAyuda) {
            Dialog(onDismissRequest = { mostrarAyuda = false }) {

                Box(
                    modifier = Modifier
                        .background(Color(0xEEAAAAAA))
                        .fillMaxSize()

                ) {

                    LazyColumn(
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        items(1) {
                            Icon(imageVector = Icons.Default.Close,
                                contentDescription = "Botón cerrar",
                                modifier = Modifier
                                    .clickable {
                                        mostrarAyuda = false
                                    }
                                    .padding(16.dp)
                            )
                            Text(
                                text = "Intrucciones de uso",
                                textAlign = TextAlign.Center,
                                textDecoration = TextDecoration.Underline,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(0.dp, 5.dp)
                            )
                            Text(
                                text = " - Operaciones básicas: ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, end = 20.dp),
                                text = "Sumar, restar, multiplicar, dividir:\n" +
                                        "A » [ + - * / ] » B » [ = ] » resultado.\n" +
                                        "Si en lugar de [=] se pulsa otra operación, " +
                                        "se muestra el resultado, y se concatena " +
                                        "a la nueva operación.",
                                fontSize = 15.sp, textAlign = TextAlign.Justify
                            )
                            Text(
                                text = " - Operaciones especiales: ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp),
                                text = "Porcentaje:\n" +
                                        "A » [ % ] » B » [ = ] » resultado.\n" +
                                        "El 'A' por ciento de 'B' es igual a resultado.\n" +
                                        "Módulo:\n" +
                                        "A » [ MOD ] » B » [ = ] » resultado.\n" +
                                        "El resto de 'A' dividido 'B' es igual a resultado.\n" +
                                        "Potencia:\n" +
                                        "A » [ x^y ] » B » [ = ] » resultado.\n" +
                                        "Base 'A' exponente 'B' es igual a resultado.\n" +
                                        "Raíz cuadrada:\n" +
                                        "A » [√] » [ = ] » resultado.\n" +
                                        "La raíz cuadrada de 'A' es igual a resultado.\n",
                                fontSize = 15.sp
                            )
                            Text(
                                text = " - Otros: ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp),
                                text = "Numeros negativos / positivos:\n" +
                                        "A » [ -/+ ] » negativo/positivo.\n" +
                                        "'A' cambia de signo al pulsar '-/+'.\n" +
                                        "Borrar:\n" +
                                        "⌫.\n" +
                                        "Elimina el dígito a la derecha del numero en pantalla.\n" +
                                        "Reiniciar:\n" +
                                        "C.\n" +
                                        "Reinicia por defecto todos los valores actuales.\n" +
                                        "Ayuda:\n" +
                                        "?.\n" +
                                        "Abre este mismo menú de ayuda.\n",
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
                .padding(5.dp, 0.dp)

        ) {


            Text(
                text = valorMostrado,
                fontFamily = digitaldisplay,
                fontSize = 52.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x55ADC6D0))
                    .align(Alignment.Center)
                    .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                    .padding(6.dp, 4.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(7f)
                .background(Color.White)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (siete, ocho, nueve, cuatro, cinco, seis, uno, dos, tres, cero) = createRefs()
                val (negPos, borrar, ayuda, reiniciar, porcentaje, modulo, potencia, raiz, dividir, multiplicar, restar, decimal, igual, sumar) = createRefs()

                ElevatedButton(onClick = {
                    if (valorMostrado.length < 15 || valorMostrado.toDouble() < 0.0) {
                        valorMostrado = truncarDecimalesCero((valorMostrado.toDouble() * -1))
                        if (focoActivo == OperandoActivo.PRIMER_OPERANDO) {
                            primerOperando = valorMostrado.toDouble()

                        } else {
                            segundoOperando = valorMostrado.toDouble()
                        }
                    }

                }, modifier = Modifier
                    .constrainAs(negPos) {
                        top.linkTo(parent.top)
                        end.linkTo(borrar.start)
                        bottom.linkTo(porcentaje.top)
                        start.linkTo(parent.start)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("+/-", 15.sp)

                }

                ElevatedButton(onClick = {
                    valorMostrado = if (valorMostrado.length > 1) {
                        valorMostrado.take(valorMostrado.length - 1)
                    } else {
                        "0"
                    }
                }, modifier = Modifier
                    .constrainAs(borrar) {
                        top.linkTo(parent.top)
                        end.linkTo(ayuda.start)
                        bottom.linkTo(modulo.top)
                        start.linkTo(negPos.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("⌫", 25.sp)
                }

                ElevatedButton(onClick = {
                    mostrarAyuda = true
                }, modifier = Modifier
                    .constrainAs(ayuda) {
                        top.linkTo(parent.top)
                        end.linkTo(reiniciar.start)
                        bottom.linkTo(potencia.top)
                        start.linkTo(borrar.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("?", 25.sp)
                }

                ElevatedButton(onClick = {
                    primerOperando = 0.0
                    segundoOperando = 0.0
                    resultadoActual = 0.0
                    valorMostrado = "0"
                    focoActivo = OperandoActivo.PRIMER_OPERANDO
                    operacionActual = OperacionActual.NINGUNA
                }, modifier = Modifier
                    .constrainAs(reiniciar) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(raiz.top)
                        start.linkTo(ayuda.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    shape = RoundedCornerShape(8.dp)) {

                    textoBoton("C", 25.sp)
                }

                ElevatedButton(onClick = {
                    onClickOperacion()
                    operacionActual = OperacionActual.PORCIENTO
                }, modifier = Modifier
                    .constrainAs(porcentaje) {
                        top.linkTo(negPos.bottom)
                        end.linkTo(modulo.start)
                        bottom.linkTo(siete.top)
                        start.linkTo(parent.start)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    colors = colorOperacion(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("%", 25.sp)
                }

                ElevatedButton(onClick = {
                    onClickOperacion()
                    operacionActual = OperacionActual.MODULO
                }, modifier = Modifier
                    .constrainAs(modulo) {
                        top.linkTo(borrar.bottom)
                        end.linkTo(potencia.start)
                        bottom.linkTo(ocho.top)
                        start.linkTo(porcentaje.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    colors = colorOperacion(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("MOD", 15.sp)
                }

                ElevatedButton(onClick = {
                    onClickOperacion()
                    operacionActual = OperacionActual.POTENCIA
                }, modifier = Modifier
                    .constrainAs(potencia) {
                        top.linkTo(ayuda.bottom)
                        end.linkTo(raiz.start)
                        bottom.linkTo(nueve.top)
                        start.linkTo(modulo.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    colors = colorOperacion(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("x^y", 15.sp)
                }

                ElevatedButton(onClick = {
                    if (focoActivo == OperandoActivo.PRIMER_OPERANDO) {
                        operacionActual = OperacionActual.RAIZ
                        resultadoActual = valorMostrado.toDouble()
                        operar()
                        valorMostrado = truncarDecimalesCero(resultadoActual)
                    } else {
                        segundoOperando = valorMostrado.toDouble()
                        operar()
                        operacionActual = OperacionActual.RAIZ
                        operar()
                        valorMostrado = truncarDecimalesCero(resultadoActual)
                        primerOperando = valorMostrado.toDouble()
                        segundoOperando = 0.0
                        focoActivo = OperandoActivo.SEGUNDO_OPERANDO
                    }
                    operacionActual = OperacionActual.NINGUNA
                }, modifier = Modifier
                    .constrainAs(raiz) {
                        top.linkTo(borrar.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(dividir.top)
                        start.linkTo(potencia.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    colors = colorOperacion(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("√", 25.sp)
                }
                ElevatedButton(onClick = {
                    onClickNumero("7")
                }, modifier = Modifier
                    .constrainAs(siete) {
                        top.linkTo(raiz.bottom)
                        end.linkTo(ocho.start)
                        bottom.linkTo(cuatro.top)
                        start.linkTo(parent.start)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("7", 25.sp)
                }

                ElevatedButton(onClick = {
                    onClickNumero("8")
                }, modifier = Modifier
                    .constrainAs(ocho) {
                        top.linkTo(modulo.bottom)
                        end.linkTo(nueve.start)
                        bottom.linkTo(cinco.top)
                        start.linkTo(siete.end)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("8", 25.sp)
                }

                ElevatedButton(onClick = {
                    onClickNumero("9")
                }, modifier = Modifier
                    .constrainAs(nueve) {
                        top.linkTo(potencia.bottom)
                        end.linkTo(dividir.start)
                        bottom.linkTo(seis.top)
                        start.linkTo(ocho.end)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("9", 25.sp)
                }
                ElevatedButton(onClick = {
                    onClickOperacion()
                    operacionActual = OperacionActual.DIVIDE
                }, modifier = Modifier
                    .constrainAs(dividir) {
                        top.linkTo(raiz.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(multiplicar.top)
                        start.linkTo(nueve.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    colors = colorOperacion(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("/", 25.sp)
                }

                ElevatedButton(onClick = {
                    onClickNumero("4")
                }, modifier = Modifier
                    .constrainAs(cuatro) {
                        top.linkTo(siete.bottom)
                        end.linkTo(cinco.start)
                        bottom.linkTo(uno.top)
                        start.linkTo(parent.start)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("4", 25.sp)
                }

                ElevatedButton(onClick = {
                    onClickNumero("5")
                }, modifier = Modifier
                    .constrainAs(cinco) {
                        top.linkTo(ocho.bottom)
                        end.linkTo(seis.start)
                        bottom.linkTo(dos.top)
                        start.linkTo(cuatro.end)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("5", 25.sp)
                }

                ElevatedButton(onClick = {
                    onClickNumero("6")
                }, modifier = Modifier
                    .constrainAs(seis) {
                        top.linkTo(nueve.bottom)
                        end.linkTo(multiplicar.start)
                        bottom.linkTo(tres.top)
                        start.linkTo(cinco.end)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("6", 25.sp)
                }
                ElevatedButton(onClick = {
                    onClickOperacion()
                    operacionActual = OperacionActual.MULTIPLICA

                }, modifier = Modifier
                    .constrainAs(multiplicar) {
                        top.linkTo(dividir.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(restar.top)
                        start.linkTo(seis.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    colors = colorOperacion(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("*", 25.sp)
                }
                ElevatedButton(onClick = {
                    onClickNumero("1")
                }, modifier = Modifier
                    .constrainAs(uno) {
                        top.linkTo(cuatro.bottom)
                        end.linkTo(dos.start)
                        bottom.linkTo(decimal.top)
                        start.linkTo(parent.start)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("1", 25.sp)
                }

                ElevatedButton(onClick = {
                    onClickNumero("2")
                }, modifier = Modifier
                    .constrainAs(dos) {
                        top.linkTo(cinco.bottom)
                        end.linkTo(tres.start)
                        bottom.linkTo(cero.top)
                        start.linkTo(uno.end)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("2", 25.sp)
                }

                ElevatedButton(onClick = {
                    onClickNumero("3")
                }, modifier = Modifier
                    .constrainAs(tres) {
                        top.linkTo(seis.bottom)
                        end.linkTo(restar.start)
                        bottom.linkTo(igual.top)
                        start.linkTo(dos.end)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("3", 25.sp)
                }
                ElevatedButton(onClick = {
                    onClickOperacion()
                    operacionActual = OperacionActual.RESTA
                }, modifier = Modifier
                    .constrainAs(restar) {
                        top.linkTo(multiplicar.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(sumar.top)
                        start.linkTo(tres.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    colors = colorOperacion(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("-", 25.sp)
                }
                ElevatedButton(onClick = {
                    if (valorMostrado.length in 1..13 && !valorMostrado.contains(".")) {
                        valorMostrado += "."
                    }
                }, modifier = Modifier
                    .constrainAs(decimal) {
                        top.linkTo(uno.bottom)
                        end.linkTo(cero.start)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton(".", 25.sp)
                }


                ElevatedButton(onClick = {
                    if (focoActivo == OperandoActivo.ESTADO_INTERMEDIO) {
                        valorMostrado = "0"
                        focoActivo = OperandoActivo.SEGUNDO_OPERANDO
                    }
                    if ((valorMostrado.length == 1 && valorMostrado != "0") || (valorMostrado.length in 2..14)) {
                        valorMostrado += "0"
                    }
                }, modifier = Modifier
                    .constrainAs(cero) {
                        top.linkTo(dos.bottom)
                        end.linkTo(igual.start)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(decimal.end)
                    }
                    .width(80.dp)
                    .height(80.dp), colors = colorNumero(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("0", 25.sp)
                }
                ElevatedButton(onClick = {
                    segundoOperando = valorMostrado.toDouble()
                    operar()
                    valorMostrado = truncarDecimalesCero(resultadoActual)
                    primerOperando = resultadoActual
                    segundoOperando = 0.0
                    resultadoActual = 0.0
                    focoActivo = OperandoActivo.PRIMER_OPERANDO

                }, modifier = Modifier
                    .constrainAs(igual) {
                        top.linkTo(tres.bottom)
                        end.linkTo(sumar.start)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(cero.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4)),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("=", 25.sp)
                }
                ElevatedButton(onClick = {
                    onClickOperacion()
                    operacionActual = OperacionActual.SUMA
                }, modifier = Modifier
                    .constrainAs(sumar) {
                        top.linkTo(restar.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(igual.end)
                    }
                    .width(80.dp)
                    .height(80.dp),
                    colors = colorOperacion(),
                    shape = RoundedCornerShape(8.dp)) {
                    textoBoton("+", 25.sp)
                }
            }
        }
    }
}

@Composable
fun colorNumero(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = Color(0xFFF1F3F4),
        contentColor = Color(0xFF000000)
    )
}

@Composable
fun colorOperacion(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = Color(0xFFDADCE0),
        contentColor = Color(0xFF000000)
    )
}






