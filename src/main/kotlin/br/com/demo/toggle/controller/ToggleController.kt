package br.com.demo.toggle.controller

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.togglz.core.manager.FeatureManager
import org.togglz.core.util.NamedFeature
import java.io.*
import java.time.LocalDate
import java.util.UUID

@RestController
@RequestMapping("/toggle")
class ToggleController {

    @Autowired
    lateinit var featureManager: FeatureManager

    @GetMapping
    fun execute1(): ResponseEntity<Any> {
        return if (featureManager.isActive(NamedFeature("TOGGLE_CONTROLLER"))) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.unprocessableEntity().build()
        }
    }

    @PostMapping("/csv")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile): ResponseEntity<Any> {
        val listColor = mutableListOf<Color>()
        BufferedReader(InputStreamReader(file.inputStream, "UTF-8")).use {
            for(o: CSVRecord in CSVParser.parse(it, CSVFormat.DEFAULT).records) {
                listColor.add(if(o.recordNumber != 1L) Color(o.values()[0], o.values()[1], o.values()[2], o.values()[3], o.values()[4]) else continue)
            }
        }
        return ResponseEntity.ok(listColor)
    }

    @GetMapping("/csv/download")
    fun handleFileDownload(): ResponseEntity<Any> {
        val out = ByteArrayOutputStream()
        val csvPrinter = CSVPrinter(PrintWriter(out), CSVFormat.DEFAULT)

        csvPrinter.printRecord(getColunas())
        getValues().forEach { csvPrinter.printRecord(it) }
        csvPrinter.flush()

        val file = InputStreamResource(ByteArrayInputStream(out.toByteArray()))
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "clubes-report-${LocalDate.now()}.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file)
    }
}

fun getColunas(): MutableList<String> {
    return mutableListOf("id", "name", "description")
}

fun getValues(): MutableList<MutableList<String>> {
    val list = mutableListOf<MutableList<String>>()
    repeat(100) {
        list.add(mutableListOf(UUID.randomUUID().toString(), "name ${UUID.randomUUID()}", "description ${UUID.randomUUID()}"))
    }
    return list
}

data class Color(val id: String, val name: String, val description: String, val teste: String, val hoje: String)