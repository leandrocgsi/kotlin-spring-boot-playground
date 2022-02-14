package br.com.erudio.data.vo.v1

class UploadFileResponseVO {

    var fileName: String? = null
    var fileDownloadUri: String? = null
    var fileType: String? = null
    var size: Long = 0

    constructor()
    constructor(fileName: String?, fileDownloadUri: String?, fileType: String?, size: Long) {
        this.fileName = fileName
        this.fileDownloadUri = fileDownloadUri
        this.fileType = fileType
        this.size = size
    }
}