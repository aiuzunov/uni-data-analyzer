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
    }
    ,

    downloadFiles: function (formType, onSuccess, onError) {
        let formData = new FormData();
        if(formType == "two-files"){
            formData.append("selected-analysis",$("#selected-analysis").val() );
            formData.append("analysis-type",$("#analysis-type").val() );
        }
        else if(formType == "zip"){
            formData.append("selected-analysis",$("#zip-selected-analysis").val() );
            formData.append("analysis-type",$("#zip-analysis-type").val() );
        }
        else
        {
            $("#file2-error").text("WTF");
            $("#file2-error").show();
        }


        $.ajax({
            type: 'POST',
            method: 'POST',
            xhrFields: {
                        responseType: 'blob'
                    },
            processData: false,
            contentType: false,
            cache: false,
            url: '/analyse/download',
            data: formData,
            success: onSuccess,
            error: onError
        });
    }
}