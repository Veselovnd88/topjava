const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "calories"
                },
                {
                    "data": "description"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

function updateTableFiltered() {
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + 'filter',
        data: $('#filterParams').serialize(),
        success: function (data) {
            ctx.datatableApi.clear().rows.add(data).draw();
        }
    })
}

function resetFilter() {
    $("#filterParams")[0].reset();
    updateTable();
}