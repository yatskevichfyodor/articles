Dropzone.autoDiscover = false;

var dropzone = new Dropzone('#dropzone', {
    url: "/image-upload",
    paramName: "file", // The name that will be used to transfer the file
    maxFilesize: 2, // MB
    maxFiles: 1,
    addRemoveLinks: true,
    autoProcessQueue: false,
    acceptedFiles: '.jpg,.png,.jpeg,.gif',
    resizeWidth: 640,
    resizeHeight: 360,
    resizeMethod: 'crop',
    dictDefaultMessage: LANG.dropzone_defaultMessage,
    dictInvalidFileType: LANG.dropzone_filetype,
    dictResponseError: LANG.dropzone_upload,
    dictRemoveFile: LANG.dropzone_remove,
    dictMaxFilesExceeded: LANG.dropzone_maxFilesExceeded,
    dictFileTooBig: LANG.dropzone_fileTooBig
});

dropzone.on("dragend", function(file) {

});

function uploadImageOnCloud() {
    console.log(dropzone);
    var imageUrl;

    const data = new FormData();
    data.append('file', dropzone.getAcceptedFiles()[0]);
    data.append('upload_preset', 'preset');
    data.append('api_key', 924795638924219);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', "https://api.cloudinary.com/v1_1/vasilyyatskevich/image/upload", false);
    const sendImage = xhr.send(data);
    const imageResponse = xhr.responseText;
    console.log('Response: ', imageResponse);
    var jsonResponse = JSON.parse(imageResponse);
    imageUrl = jsonResponse["url"];

    return imageUrl;
}

function uploadImageOnServer() {
    console.log(dropzone);
    var imagePath;

    dropzone.processQueue();
}

dropzone.on("success", function(file, serverResponse) {
    continueArticleUpload(file);
});