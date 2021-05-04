$filesCorrelationAnalysisBtn = $("#files-correlation-analysis-btn");
$filesDispersionAnalysisBtn = $("#files-dispersion-analysis-btn");
$filesCentralTrendAnalysisBtn = $("#files-central-trend-analysis-btn");
$filesFrequencyAnalysisBtn = $("#files-frequency-analysis-btn");

$zipCorrelationAnalysisBtn = $("#zip-correlation-analysis-btn");
$zipDispersionAnalysisBtn = $("#zip-dispersion-analysis-btn");
$zipCentralTrendAnalysisBtn = $("#zip-central-trend-analysis-btn");
$zipFrequencyAnalysisBtn = $("#zip-frequency-analysis-btn");

const FILES_COLUMN_NAME = "files";
const ZIP_COLUMN_NAME = "zip";

const ENTRIES = "entries";

$filesCorrelationAnalysisBtn.click({column: FILES_COLUMN_NAME}, correlationAnalysis);
$zipCorrelationAnalysisBtn.click({column: ZIP_COLUMN_NAME}, correlationAnalysis);

$filesDispersionAnalysisBtn.click({column: FILES_COLUMN_NAME}, dispersionAnalysis);
$zipDispersionAnalysisBtn.click({column: ZIP_COLUMN_NAME}, dispersionAnalysis);

$filesCentralTrendAnalysisBtn.click({column: FILES_COLUMN_NAME}, centralTrendAnalysis);
$zipCentralTrendAnalysisBtn.click({column: ZIP_COLUMN_NAME}, centralTrendAnalysis);

$filesFrequencyAnalysisBtn.click({column: FILES_COLUMN_NAME}, frequencyAnalysis);
$zipFrequencyAnalysisBtn.click({column: ZIP_COLUMN_NAME}, frequencyAnalysis);

function dispersionAnalysis(event) {
    const column = event.data.column + '-column';
    const $analysisButtonId = "#" + event.data.column + "-dispersion-analysis-btn";
    setDisabledProperty($analysisButtonId, true);

    const errorId = column + '-dispersion-error';
    const columnId = '#' + column;
    const spinnerId = column + "-dispersion-spinner";

    showAnalysisSpinner(spinnerId, column);

    $('#' + errorId).remove();

    const cardId = column + '-dispersion-analysis-card'
    $('#' + cardId).remove();

    function onSuccess(response) {
        let $template = $('#dispersion-analysis-template').html();

        $template = $template.replace("{{dispersionCardId}}", cardId)
            .replace("{{scope}}", response["scope"])
            .replace("{{dispersion}}", response["dispersion"])
            .replace("{{standardDeviation}}", response["standardDeviation"]);

        $(columnId).prepend($template);
        hideAnalysisSpinner(spinnerId, column);
        setDisabledProperty($analysisButtonId, false);
    }

    function onError(response) {
        let $template = $('#error-message-template').html();

        $template = $template.replace("{{id}}", errorId)
            .replace("{{errorName}}", "Мерки на разсейване за прегледани лекции")
            .replace("{{error}}", response["responseJSON"]["error"]);

        $(columnId).prepend($template);
        hideAnalysisSpinner(spinnerId, column);
        setDisabledProperty($analysisButtonId, false);
    }

    RestApiClient.generateAnalysis("dispersion", onSuccess, onError);
}

function correlationAnalysis(event) {
    const column = event.data.column + '-column';
    const $analysisButtonId = "#" + event.data.column + "-correlation-analysis-btn";
    setDisabledProperty($analysisButtonId, true);

    const errorId = column + '-correlation-error';
    const columnId = '#' + column;
    const spinnerId = column + "-correlation-spinner";

    showAnalysisSpinner(spinnerId, column);

    $('#' + errorId).remove();

    const cardId = column + '-correlation-analysis-card'
    $('#' + cardId).remove();

    function onSuccess(response) {
        let $rowTemplate = "";
        Object.values(response[ENTRIES]).forEach(entry => {
            $rowTemplate += $('#correlation-analysis-row-template').html()
                .replace("{{fileName}}", entry["name"])
                .replace("{{correlationCoefficient}}", entry["correlationCoefficient"]);
        })

        const $template = $("#correlation-analysis-template").html()
            .replace("{{correlationCardId}}", cardId)
            .replace("{{rows}}", $rowTemplate);

        $(columnId).prepend($template);
        hideAnalysisSpinner(spinnerId, column);
        setDisabledProperty($analysisButtonId, false);
    }

    function onError(response) {
        let $template = $('#error-message-template').html();

        $template = $template.replace("{{id}}", errorId)
            .replace("{{errorName}}", "Корелационен анализ")
            .replace("{{error}}", response["responseJSON"]["error"]);

        $(columnId).prepend($template);
        hideAnalysisSpinner(spinnerId, column);
        setDisabledProperty($analysisButtonId, false);
    }

    RestApiClient.generateAnalysis("correlation", onSuccess, onError);
}

function frequencyAnalysis(event) {
    const column = event.data.column + '-column';
    const $analysisButtonId = "#" + event.data.column + "-frequency-analysis-btn";
    setDisabledProperty($analysisButtonId, true);

    const errorId = column + '-frequency-analysis-error';
    const columnId = '#' + column;
    const spinnerId = column + "-frequency-analysis-spinner";

    showAnalysisSpinner(spinnerId, column);

    $('#' + errorId).remove();

    const cardId = column + '-frequency-analysis-card'
    $('#' + cardId).remove();

    function onSuccess(response) {
        let $rows = "";
        response[ENTRIES].forEach(value => {
            const $row = $('#frequency-analysis-row-template').html()
                .replace("{{name}}", value["name"])
                .replace("{{absoluteFrequency}}", value["absoluteFrequency"])
                .replace("{{relativeFrequency}}", value["relativeFrequency"]);
            $rows += $row;
        });

        const $template = $('#frequency-analysis-template').html()
            .replace("{{frequencyAnalysisCardId}}", cardId)
            .replace("{{rows}}", $rows);

        $(columnId).prepend($template);
        hideAnalysisSpinner(spinnerId, column);
        setDisabledProperty($analysisButtonId, false);
    }

    function onError(response) {
        let $template = $('#error-message-template').html();

        $template = $template.replace("{{id}}", errorId)
            .replace("{{errorName}}", "Честотно резпределение на прегледани лекции")
            .replace("{{error}}", response["responseJSON"]["error"]);

        $(columnId).prepend($template);
        hideAnalysisSpinner(spinnerId, column);
        setDisabledProperty($analysisButtonId, false);
    }

    RestApiClient.generateAnalysis("frequency", onSuccess, onError);
}

function centralTrendAnalysis(event) {
    const column = event.data.column + '-column';
    const $analysisButtonId = "#" + event.data.column + "-central-trend-analysis-btn";
    setDisabledProperty($analysisButtonId, true);

    const errorId = column + '-central-trend-error';
    const columnId = '#' + column;
    const spinnerId = column + "-central-trend-spinner";

    showAnalysisSpinner(spinnerId, column);

    $('#' + errorId).remove();

    const cardId = column + '-central-trend-analysis-card'
    $('#' + cardId).remove();

    function onSuccess(response) {
        let $template = $('#central-trend-analysis-template').html();

        $template = $template.replace("{{centralTrendCardId}}", cardId)
            .replace("{{mean}}", response["mean"])
            .replace("{{median}}", response["median"])
            .replace("{{modes}}", response["modes"]);

        $(columnId).prepend($template);
        hideAnalysisSpinner(spinnerId, column);
        setDisabledProperty($analysisButtonId, false);
    }

    function onError(response) {
        let $template = $('#error-message-template').html();

        $template = $template.replace("{{id}}", errorId)
            .replace("{{errorName}}", "Мерки на централната тенденция за прегледани лекции")
            .replace("{{error}}", response["responseJSON"]["error"]);

        $(columnId).prepend($template);
        hideAnalysisSpinner(spinnerId, column);
        setDisabledProperty($analysisButtonId, false);
    }

    RestApiClient.generateAnalysis("central-trend", onSuccess, onError);
}

function showAnalysisSpinner(spinnerId, columnId) {
    const $spinnerTemplate = $('#loading-spinner-template').html()
        .replace("{{spinner-id}}", spinnerId);

    $('#' + columnId).prepend($spinnerTemplate);
}

function hideAnalysisSpinner(spinnerId) {
    $('#' + spinnerId).remove();
}

function setDisabledProperty(id, value) {
    $(id).prop("disabled", value)
}