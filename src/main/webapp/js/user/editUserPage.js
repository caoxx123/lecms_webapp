$("document").ready(function(){
	
	var op = $("#op").val();
	var role = $("#role").val();
	
	if("view"==op){
		$("#sub").hide();
		$("input[type='text']").attr("readonly","readonly").addClass("readonly").css("background-color","C1C1C1");
		$("input[type='checkbox']").attr("readonly","readonly").addClass("readonly").css("background-color","C1C1C1");
		$("select").attr("readonly","readonly").addClass("readonly").css("background-color","C1C1C1");
		$("input[name='passWord']").val("用户密码已经被加密！");
	}
	
	if("edit"==op){
		$("input[name='loginName']").attr("readonly","readonly").addClass("readonly").css("background-color","C1C1C1");
	}
	
	/*这儿需要一个登录名的异步校验*/
	$("input[name='loginName']").on("change",function(){
		var currentLoginName = $(this).val();
		$.ajax({
			url:"/lecms_webapp/UserController/checkLoginName",
			type:"POST",
			data: {"loginName":currentLoginName},
			success:function(data){
				if("true"!=data){
					$("input[name='loginName']").attr("placeholder",currentLoginName+"  该名称已被注册，请尝试换一个！").val("").focus();
					alert("登录名已被占用！");
				}
			}
		})
	});
	
//	确认按钮
	$("#sub").on("click",function(){
		
		var valid = $(".userEditForm").valid();
		if(valid){
			var id=$("input[name='id']").val();
			var name=$("input[name='name']").val();
			var loginName = $("input[name='loginName']").val();
			var passWord = $("input[name='passWord']").val();
			var mail = $("input[name='mail']").val();
			var leval = $("option:selected").val();
			var deleted = $("em").text();
			if("启用"==deleted){
				deleted = false;
			}else{
				deleted = true;
			}
			
			console.info(id+"-"+name+"-"+loginName+"-"+passWord+"-"+mail+"-"+leval+"-"+deleted);
			
			$.ajax({
				url:"/lecms_webapp/UserController/saveUser",
				type:"POST",
				data: {"id":id,"name":name,"loginName":loginName,"passWord":passWord,"mail":mail,"leval":leval,"deleted":deleted},
				success:function(){
					$(location).attr("href","/lecms_webapp/UserController/UserList?role="+role);
				}
			})
		}
		
	});
	
//	取消按钮
	$("#cans").on("click",function(){
		$(location).attr("href","/lecms_webapp/UserController/UserList?role="+role);
	});
});