$("#file1-file-picker").change(enableUploadBtn);

$("#file2-file-picker").change(enableUploadBtn);

$("#file2-upload-btn").click(uploadFiles);

$("#zip-upload-btn").click(function () {
    uploadZip();
});


$("#zip-file-picker").change(function () {
    $("#zip-upload-btn").prop('disabled', !($(this).val() != ""));
});

function arePickersFilled() {
    let filePicker1 = $('#file1-file-picker');
    let filePicker2 = $('#file2-file-picker');
    return filePicker1.val() != "" && filePicker2.val() != ""
}

function enableUploadBtn() {
    $('#file2-upload-btn').prop('disabled', !arePickersFilled());
}

function uploadFiles() {
    if (arePickersFilled()) {
        let $progress = $("#files-uploading-progress");
        $("#file2-error").hide();
        $("#files-uploading-progress").show();
        RestApiClient.uploadFiles(
            getFile($('#file1-file-picker')),
            getFile($('#file2-file-picker')),
            function () {
                $progress.hide();
                enableAnalysisBtns(true, false)
            },
            function () {
                $progress.hide();
                $("#file2-error").show();
                enableAnalysisBtns(false, false)
            });
    } else {
        // Here there should be error message just in case.
    }
}

function uploadZip() {
    let $progress = $("#zip-uploading-progress")
    if ($("#zip-file-picker").val() != "") {
        $progress.show();
        $("#zip-error").hide();

        RestApiClient.uploadFile(getFile($("#zip-file-picker")), function () {
            $("#zip-error").hide();
            $progress.hide();
            $("#zip-upload-btn").prop('disabled', true);
            enableAnalysisBtns(true, true)
        }, function (data) {
            $("#zip-error").show();
            $progress.hide();
            $("#zip-upload-btn").prop('disabled', false);
            enableAnalysisBtns(false, true)
        });
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