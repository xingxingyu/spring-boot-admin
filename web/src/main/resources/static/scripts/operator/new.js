$(function(){
	$('#saveForm').validate({
        rules: {
            operatorId       :{required:true,digits:true},
            operatorName      :{required:true},
            departmentId       :{required:true,digits:true},
            department      :{required:true}
        },messages:{
            operatorId :{required:"必填整数"},
            operatorName :{required:"必填"},
            departmentId :{required:"必填整数"},
            department :{required:"必填"}
        }
 	});
$('.saveBtn').click(function(){
	 if($('#saveForm').valid()){
         $.ajax({
             type: "POST",
             url: "./save",
             data: $("#saveForm").serialize(),
             headers: {"Content-type": "application/x-www-form-urlencoded;charset=UTF-8"},
             success: function (data) {
                 if (data == 1) {
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

