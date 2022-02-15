package br.com.erudio.controller

import br.com.erudio.data.vo.v1.UploadFileResponseVO
import br.com.erudio.services.FileStorageService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/api/file/v1")
class FileController {

    private val logger = LoggerFactory.getLogger(FileController::class.java)

    @Autowired
    private val fileStorageService: FileStorageService? = null

    @PostMapping("/uploadFile")
    fun uploadFile(@RequestParam("file") file: MultipartFile): UploadFileResponseVO {
        val fileName = fileStorageService!!.storeFile(file)
        val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/file/v1/downloadFile/")
            .path(fileName)
            .toUriString()
        return UploadFileResponseVO(fileName, fileDownloadUri, file.contentType, file.size)
    }

    @PostMapping("/uploadMultipleFiles")
    fun uploadMultipleFiles(@RequestParam("files") files: Array<MultipartFile>): List<UploadFileResponseVO> {
        val uploadFileResponseVOs = arrayListOf<UploadFileResponseVO>()
        for (file in files) {
            var uploadFileResponseVO: UploadFileResponseVO = uploadFile(file)
            uploadFileResponseVOs.add(uploadFileResponseVO)
        }
        return uploadFileResponseVOs
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    fun downloadFile(@PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource> {
        val resource = fileStorageService!!.loadFileAsResource(fileName)
        var contentType: String? = null
        try {
            contentType = request.servletContext.getMimeType(resource.file.absolutePath)
        } catch (e: Exception) {
            logger.info("Could not determine file type!")
        }
        if (contentType == null) {
            contentType = "application/octet-stream"
        }
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.filename + "\"")
            .body(resource)
    }
}