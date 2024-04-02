const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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
                    "asc"
                ]
            ]
        })
    );
});

function changeStatus(checkbox, userId) {
    const status = checkbox.checked;
    const trElement = $(checkbox).closest('tr');

    $.ajax({
            url: ctx.ajaxUrl + userId + "/enable?enabled=" + status,
            type: "PATCH"
        }
    ).done(function () {
        successNoty("User status updated");
        $(trElement).attr('user-enabled', status);
    }).fail(function () {
        $(trElement).attr('user-enabled', !status);
        checkbox.checked = !status
    });
}