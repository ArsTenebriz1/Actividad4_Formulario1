package com.example.myapplication1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// Estructura de datos para almacenar temporalmente la información introducida
data class DatosFormulario(
    val nombre: String,
    val matricula: String,
    val asignatura: String,
    val hora: String,
    val fecha: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    // Estados para almacenar el valor de cada campo de texto
    var nombre by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var asignatura by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    // Estado para gestionar los mensajes de validación y la tarjeta de resultados
    var mensajeError by remember { mutableStateOf("") }
    var datosGuardados by remember { mutableStateOf<DatosFormulario?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Asignaturas") }
            )
        }
    ) { paddingValues ->
        // Column con verticalScroll para garantizar diseño responsivo en pantallas pequeñas o rotadas
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Formulario de Registro",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Alumno") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo: Matrícula (Teclado Numérico)
            OutlinedTextField(
                value = matricula,
                onValueChange = { matricula = it },
                label = { Text("Matrícula") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo: Asignatura
            OutlinedTextField(
                value = asignatura,
                onValueChange = { asignatura = it },
                label = { Text("Asignatura") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo: Hora que se imparte
            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora que se imparte (ej. 09:00 AM)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo: Fecha de entrega
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha de entrega (ej. 15/10/2026)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Despliegue condicional de mensaje de error si la validación falla
            if (mensajeError.isNotEmpty()) {
                Text(
                    text = mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Botón de acción para validar y guardar
            Button(
                onClick = {
                    val matriculaValida = matricula.length == 7 && matricula.all { it.isDigit() }
                    // Expresión regular para fecha DD/MM/2026
                    val fechaRegex = Regex("""^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/2026$""")
                    // Expresión regular básica para hora (09:00 AM)
                    val horaRegex = Regex("""^(0[1-9]|1[0-2]):[0-5][0-9] (AM|PM|am|pm)$""")

                    when {
                        nombre.isBlank() || asignatura.isBlank() -> {
                            mensajeError = "Nombre y Asignatura no pueden estar vacíos."
                            datosGuardados = null
                        }
                        !matriculaValida -> {
                            mensajeError = "La matrícula debe tener exactamente 7 dígitos numéricos."
                            datosGuardados = null
                        }
                        !horaRegex.matches(hora) -> {
                            mensajeError = "Formato de hora inválido (ej: 09:00 AM)."
                            datosGuardados = null
                        }
                        !fechaRegex.matches(fecha) -> {
                            mensajeError = "La fecha debe ser del año 2026 y formato DD/MM/AAAA."
                            datosGuardados = null
                        }
                        else -> {
                            mensajeError = "" // Limpia errores previos
                            datosGuardados = DatosFormulario(
                                nombre = nombre,
                                matricula = matricula,
                                asignatura = asignatura,
                                hora = hora,
                                fecha = fecha
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Datos")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Despliegue temporal de la información en un Card bajo el botón
            datosGuardados?.let { datos ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Resumen de Datos Guardados",
                            style = MaterialTheme.typography.titleMedium
                        )
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(text = " Nombre: ${datos.nombre}")
                        Text(text = " Matrícula: ${datos.matricula}")
                        Text(text = " Asignatura: ${datos.asignatura}")
                        Text(text = " Hora: ${datos.hora}")
                        Text(text = " Fecha de Entrega: ${datos.fecha}")
                    }
                }
            }
        }
    }
}