var RestApiClient = {
    uploadFile: function (file, onSuccess, onError) {
        let formData = new FormData();
        formData.append("file", file)
        $.ajax({
            type: 'POST',
            method: 'POST',
            processData: false,
            contentType: false,
            cache: false,
            error: onError,
            url: '/analyse/upload',
            data: formData,
            success: onSuccess
        });
    },

    uploadFiles: function (firstFile, secondFile, onSuccess, onError) {
        let formData = new FormData();
        formData.append("files", firstFile)
        formData.append("files", secondFile)
        $.ajax({
            type: 'POST',
            method: 'POST',
            processData: false,
            contentType: false,
            cache: false,
            url: '/analyse/upload/files',
            data: formData,
            success: onSuccess,
            error: onError
        });
    },

    generateAnalysis: function (analysisName, onSuccess, onError) {
        const url = '/analysis/' + analysisName;

        return $.ajax({
            type: 'POST',
            method: 'POST',
            processData: false,
            contentType: false,
            cache: false,
            url: url,
            success: onSuccess,
            error: onError
        });
    }
}