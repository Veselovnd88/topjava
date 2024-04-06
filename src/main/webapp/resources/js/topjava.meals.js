const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

// http://api.jquery.com/jQuery.ajax/#using-converters
$.ajaxSetup({
    converters: {
        "text json": function (stringData) {
            return JSON.parse(stringData,
                function (key, value) {
                    return (key === 'dateTime') ? value.substring(0, 16).replace('T', ' ') : value;
                }
            );
        }
    }
});

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (data.excess) {
                    $(row).attr("data-meal-excess", true);
                } else {
                    $(row).attr("data-meal-excess", false);
                }
            }
        })
    );

    const dateOptions = {
        autoclose: true,
        todayBtn: true,
        weekStart: 1,
        todayHighlight: true,
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d',
    };

    $('#endDate').datetimepicker({
        ...dateOptions
    });

    $('#startDate').datetimepicker({
        ...dateOptions
    });

    const timeOptions = {
        datepicker: false,
        format: 'H:i'
    }

    $('#endTime').datetimepicker({
        ...timeOptions
    })

    $('#startTime').datetimepicker({
        ...timeOptions
    })

    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    })
});
