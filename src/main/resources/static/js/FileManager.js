$file1Picker = $("#file1-file-picker");
$file2Picker = $("#file2-file-picker");
$zipFilePicker = $("#zip-file-picker");
$filesUploadBtn = $("#file2-upload-btn");
$zipFileUploadBtn = $("#zip-upload-btn");

$file1Picker.change(enableUploadBtn);
$file2Picker.change(enableUploadBtn);
$zipFilePicker.change(function () {
    $("#zip-upload-btn").prop('disabled', !($(this).val() != ""));
});

$filesUploadBtn.click(uploadFiles);
$zipFileUploadBtn.click(function () {
    uploadZip();
});

function arePickersFilled() {
    return $file1Picker.val() != "" && $file2Picker.val() != ""
}

function enableUploadBtn() {
    $filesUploadBtn.prop('disabled', !arePickersFilled());
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
                $success.show();
                $progress.hide();
                enableAnalysisBtns(true, false);
                $filesUploadBtn.prop('disabled', true);
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
            $success.show();
            $error.hide();
            $progress.hide();
            $zipFileUploadBtn.prop('disabled', true);
            enableAnalysisBtns(true, true);
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