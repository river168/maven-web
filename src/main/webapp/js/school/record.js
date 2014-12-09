$(function() {
	$("#addRecord").click(function() {
		$("#recordFrm")[0].reset();
		openDialog();
	});
});
/** 打开弹出窗口 */
function openDialog() {
	var elem = document.getElementById('editRecordDiv');
	dialog({
		title : "编辑信息",
		content : elem,
		onclose : function() {
		},
		ok : function() {
			if (checkNull()) {
				$("#recordFrm").attr("action", "saveRecord");
				$("#recordFrm").submit();
				this.remove();
			}
		}
	}).show();
}
/** 查空 */
function checkNull() {
	if (!$("input[name='name']").val()) {
		alert("请输入姓名！")
		return false;
	}
	if (!$("input[name='phone']").val()) {
		alert("请输入手机号码！")
		return false;
	}
	return true;
}

/** 修改 */
function editRecord(id) {
	$.ajax({
		type : "GET",
		url : "getRecord",
		data : {
			"id" : id
		},
		success : function(data) {
			data = eval("(" + data + ")")
			$("input[name='id']").val(data.id);
			$("input[name='name']").val(data.name);
			$("input[name='borthYear']").val(data.borthYear);
			$("input[name='sex']").val(data.sex);
			$("input[name='schoolMajor']").val(data.schoolMajor);
			$("input[name='phone']").val(data.phone);
			$("input[name='tell']").val(data.tell);
			$("input[name='home']").val(data.home);
			$("input[name='company']").val(data.company);
			$("input[name='industry']").val(data.industry);
			$("input[name='work']").val(data.work);
			$("input[name='ismarry']").val(data.ismarry);
			$("input[name='email']").val(data.email);
			$("input[name='address']").val(data.address);
		}
	});
	openDialog();
}
/** 删除 */
function delRecord(id) {
	if (confirm("确认删除？")) {
		window.location.href = "delRecord?id=" + id;
	}
}
/** 通过手机号码验证是否有权限 */
function validate(id, type) {
	var elem = document.getElementById('validatePhoneDiv');
	dialog({
		title : "请验证手机号码",
		content : elem,
		onclose : function() {
		},
		ok : function() {
			$.ajax({
				type : "GET",
				url : "validatePhone",
				data : {
					"id" : id,
					"phone" : $("#vphone").val()
				},
				success : function(data) {
					if (data != '0') {
						if (type == '1') {
							editRecord(id);
						} else {
							delRecord(id);
						}
					} else {
						alert("验证失败！")
					}
				}
			});
		}
	}).show();
}
