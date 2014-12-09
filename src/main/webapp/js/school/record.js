$(function() {
	$("#addRecord").click(function() {
		$("#recordFrm")[0].reset();
		openDialog();
	});
});
/** �򿪵������� */
function openDialog() {
	var elem = document.getElementById('editRecordDiv');
	dialog({
		title : "�༭��Ϣ",
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
/** ��� */
function checkNull() {
	if (!$("input[name='name']").val()) {
		alert("������������")
		return false;
	}
	if (!$("input[name='phone']").val()) {
		alert("�������ֻ����룡")
		return false;
	}
	return true;
}

/** �޸� */
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
/** ɾ�� */
function delRecord(id) {
	if (confirm("ȷ��ɾ����")) {
		window.location.href = "delRecord?id=" + id;
	}
}
/** ͨ���ֻ�������֤�Ƿ���Ȩ�� */
function validate(id, type) {
	var elem = document.getElementById('validatePhoneDiv');
	dialog({
		title : "����֤�ֻ�����",
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
						alert("��֤ʧ�ܣ�")
					}
				}
			});
		}
	}).show();
}
