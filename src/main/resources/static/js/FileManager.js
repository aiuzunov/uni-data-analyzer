$file1Picker = $("#file1-file-picker");
$file2Picker = $("#file2-file-picker");
$zipFilePicker = $("#zip-file-picker");
$filesUploadBtn = $("#file2-upload-btn");
$zipFileUploadBtn = $("#zip-upload-btn");
$filesDownloadForm = $(".two-files-download-form");
$zipDownloadForm = $(".zip-download-form");
$filesDownloadBtn = $("#two-files-download-btn");
$zipDownloadBtn = $("#zip-download-btn");


$file1Picker.change(enableUploadBtn);
$file2Picker.change(enableUploadBtn);
$zipFilePicker.change(function () {
    $("#zip-upload-btn").prop('disabled', !($(this).val() != ""));
});

$filesUploadBtn.click(uploadFiles);
$zipFileUploadBtn.click(function () {
    uploadZip();
});

$filesDownloadBtn.click(function () {
    downloadFile($filesDownloadBtn.data('type'));
});

$zipDownloadBtn.click(function () {
    downloadFile($zipDownloadBtn.data('type'));
});



function arePickersFilled() {
    return $file1Picker.val() != "" && $file2Picker.val() != ""
}

function enableUploadBtn() {
    $filesUploadBtn.prop('disabled', !arePickersFilled());
}

function downloadFile(formType)
{
    let $downloadError;
    let $success;
    if(formType=="two-files")
    {
        $downloadError = $("#file2-error");
        $success = $("#file2-success");
    }
    else if(formType=="zip")
    {
        $downloadError = $("#zip-error");
        $success = $("#zip-success");
    }

    let $progress = $("#files-uploading-progress");
    $progress.show();
    RestApiClient.downloadFiles(
    formType,
    function (data, textStatus, request) {
     var a = document.createElement('a');
                var url = window.URL.createObjectURL(data);
                a.href = url;
                a.download = request.getResponseHeader("filename");
                document.body.append(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
        $success.text("Файловете са успешно свалени!");
        $success.show();
        $progress.hide();
    },
    function () {
        $success.hide();
        $fileError.text("Възникна грешка при свалянето на файловете!");
        $progress.hide();
        $downloadError.show();
    });

}

function uploadFiles() {
    let $fileError = $("#file2-error");
    let $success = $("#file2-success");
    if (arePickersFilled()) {
        let $progress = $("#files-uploading-progress");
        $fileError.hide();
        $progress.show();
        RestApiClient.uploadFiles(
            getFile($file1Picker),
            getFile($file2Picker),
            function () {
                $success.text("Файловете са успешно качени!");
                $success.show();
                $progress.hide();
                enableAnalysisBtns(true, false);
                $filesUploadBtn.prop('disabled', true);
                $filesDownloadForm.show();
            },
            function () {
                $success.hide();
                $fileError.text("Възникна грешка при четенето на файловете, моля проверете дали формата е правилен!");
                $progress.hide();
                $fileError.show();
                enableAnalysisBtns(false, false)
            });
    } else {
        $success.hide();
        $fileError.text("Моля изберете файлове, които да бъдат качени!");
        $fileError.show();
    }
}

function uploadZip() {
    let $error = $("#zip-error");
    let $success = $("#zip-success");
    if ($zipFilePicker.val() != "") {
        let $progress = $("#zip-uploading-progress")
        $progress.show();
        $error.hide();

        RestApiClient.uploadFile(getFile($("#zip-file-picker")), function () {
            $success.text("Файловете са успешно качени!");
            $success.show();
            $error.hide();
            $progress.hide();
            $zipFileUploadBtn.prop('disabled', true);
            enableAnalysisBtns(true, true);
            $zipDownloadForm.show();
        }, function (data) {
            $success.hide();
            $error.text("Възникна грешка при четенето на файла, моля проверете дали формата е правилен!");
            $error.show();
            $progress.hide();
            $zipFileUploadBtn.prop('disabled', false);
            enableAnalysisBtns(false, true)
        });
    } else {
        $success.hide();
        $error.text("Моля изберете файл, който да бъде качен!");
        $error.show();
    }
}

function getFile($picker) {
    return $picker.get(0).files[0];
}

function enableAnalysisBtns(enabled, isForZip) {
    if (isForZip) {
        $('.zip-file').each((_, el) => {
            $(el).prop('disabled', !enabled)
        })
    } else {
        $('.two-files').each((_, el) => {
            $(el).prop('disabled', !enabled)
        })
    }
}