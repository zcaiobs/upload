package br.com.demo.toggle.controller

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.togglz.core.manager.FeatureManager
import org.togglz.core.util.NamedFeature
import java.io.BufferedReader
import java.io.InputStreamReader

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
}

data class Color(val id: String, val name: String, val description: String, val teste: String, val hoje: String)