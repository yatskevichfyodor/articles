var actionArr=[];

function disableAll() {
	$(".mybtn-del").attr("disabled", "disabled");
		$(".mybtn-bl").attr("disabled", "disabled");
		$(".mybtn-unbl").attr("disabled", "disabled");
		$(".mybtn-adm").attr("disabled", "disabled");
		$(".mybtn-disrank").attr("disabled", "disabled");
}

$(".chkbx").change(function(){
	// $(".mainchkbx").prop('checked', false);
	disableAll();
    actionArr = $(".chkbx:checked").map(function(i,el){
		if ($(el).hasClass("blocked")){
		    $(".mybtn-unbl").removeAttr("disabled");
		}
		else {
		    $(".mybtn-bl").removeAttr("disabled");
		}
		$(".mybtn-del").removeAttr("disabled");
        if ($(el).hasClass("admin")){
            $(".mybtn-disrank").removeAttr("disabled");
		}
		else {
            $(".mybtn-adm").removeAttr("disabled");
		}
        return $(el).val();
    }).get();
	console.log(actionArr);
})


$(".mybtn-del").click(function(){
	$.ajax({
    type: 'GET',
    url: '/admin/action/delete',
    data: {idArray: actionArr},
    contentType: 'application/json; charset=utf-8',
    dataType: 'json',
    complete: function(){
    	window.location.reload("true");
    },
    timeout: 500
});
})

$(".mybtn-bl").click(function(){
	$.ajax({
    type: 'GET',
    url: '/admin/action/block',
    data: {idArray: actionArr},
    contentType: 'application/json; charset=utf-8',
    dataType: 'json',
    complete: function(){
    	window.location.reload("true");
    },
    timeout: 500
});
})

$(".mybtn-unbl").click(function(){
	$.ajax({
    type: 'GET',
    url: '/admin/action/unblock',
    data: {idArray: actionArr},
    contentType: 'application/json; charset=utf-8',
    dataType: 'json',

    complete: function(){
    	window.location.reload("true");
    },
    timeout: 500
});
})

$(".mybtn-adm").click(function(){
	$.ajax({
    type: 'GET',
    url: '/admin/action/admin',
    data: {idArray: actionArr},
    contentType: 'application/json; charset=utf-8',
    dataType: 'json',
    complete: function(){
    	window.location.reload("true");
    },
    timeout: 500
});
})

$(".mybtn-disrank").click(function(){
	$.ajax({
    type: 'GET',
    url: '/admin/action/disrank',
    data: {idArray: actionArr},
    contentType: 'application/json; charset=utf-8',
    dataType: 'json',
    complete: function(){
    	window.location.reload("true");
    },
    timeout: 500
});
})