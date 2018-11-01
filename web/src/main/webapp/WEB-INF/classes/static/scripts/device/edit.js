$(function(){
	$('#saveForm').validate({
		rules: {
            num       :{required:true},
            ip      :{required:true},
            port      :{required:true}
		},messages:{
			num :{required:"必填"},
			ip :{required:"必填"},
			port :{required:"必填"}
        }
 	});
$('.saveBtn').click(function(){
	 if($('#saveForm').valid()){
         $.ajax({
             type: "POST",
             url: "./update",
             data: $("#saveForm").serialize(),
             headers: {"Content-type": "application/x-www-form-urlencoded;charset=UTF-8"},
             success: function (data) {
                 if (data == 1) {
                     alert("更新成功");
                     pageaction();
                     closeDialog();
                 } else {
                     alert(data);
                 }
             }
         });
		 }else{
			 alert('数据验证失败，请检查！');
		 }
	});
});

