const createBtn = document.getElementById('create-btn');

if(createBtn){
	createBtn.addEventListener('click', (event) =>{
		/*fetch("", {
			method: 'POST',
			headers: {
				"Content-Type" : "application/json",
			},
			body: JSON.stringify({
				title: document.getElementById("title").value,
				content: document.getElementById("content").value
			}),
		}).then(() => {
			alert("게시글이 등록되었습니다.");
			location.replace('/article');
		});
		*/
		body = JSON.stringify({
			title: document.getElementById("title").value,
			content: document.getElementById("content").value,
		});
		function success(){
			alert("게시글이 등록되었습니다.");
			location.replace('/article');
		}
		function fail(){
			alert("게시글 등록이 실패되었습니다.");
			location.replace('/article');
		}
		httpRequest("POST", "/additional/article", body, success, fail);
	});
}

const deleteBtn = document.getElementById('delete-btn');

if(deleteBtn) {
	deleteBtn.addEventListener('click', (event) => {
		let id = document.getElementById('article-id').value;
		/*
		fetch(`/articles/${id}`, {
			method: 'DELETE'
		}).then(() =>{
			alert('게시글이 삭제 되었습니다.');
			location.replace('/article');
		});
		*/
		function success(){
			alert("게시글이 삭제되었습니다.");
			location.replace('/article');
		}
		function fail(){
			alert("게시글 삭제가 실패되었습니다.");
			location.replace('/article');
		}
		httpRequest("DELETE", "/articles/" + id, null, success, fail);
	});
}

const updateBtn = document.getElementById('update-btn');

if(updateBtn){
	updateBtn.addEventListener('click', (evnet) =>{
		let params = new URLSearchParams(location.search);
		let id = params.get('id');
		/*
		fetch(`/articles/${id}`, {
			method: 'PUT',
			headers: {
				"Content-Type" : "application/json",
			},
			body: JSON.stringify({
				title: document.getElementById("title").value,
				content: document.getElementById("content").value
			})
		}).then(() => {
			alert('게시글 수정 완료');
			location.replace(`/article/${id}`);
		})
		*/
		body = JSON.stringify({
			title: document.getElementById("title").value,
			content: document.getElementById("content").value,
		});
		function success(){
			alert("게시글이 수정되었습니다.");
			location.replace('/article/' + id);
		}
		function fail(){
			alert("게시글 수정에 실패되었습니다.");
			location.replace('/article/' + id);
		}
		httpRequest("PUT", "/articles/" + id, body, success, fail);
	});
}

function getCookie(key){
	var result = null;
	var cookie = document.cookie.split(";");
	cookie.some(function (item){
		item = item.replace(" ", "");
		
		var dic = item.split("=");
		
		if(key === dic[0]){
			result = dic[1];
			return true;
		}
	});
	return result;
}

function httpRequest(method, url, body, success, fail){
	fetch(url, {
		method: method,
		headers: {
			Authorization: "Bearer " + localStorage.getItem("access_token"),
			"Content-Type" : "application/json",
		},
		body: body,
	}).then((resp) => {
		if(resp.status===200 || resp.status===201){
			return success();
		}
		const refreshToken = getCookie("refreshToken");
		if(resp.status===401 && refreshToken){
			fetch("/additional/token", {
				method: "POST",
				headers: {
					Authorization: "Bearer " + localStorage.getItem("access_token"),
					"Content-Type" : "application/json",
				},
				body: JSON.stringify({
					refreshToken: getCookie("refreshToken"),
				}),
			}).then((res) => {
				if(res.ok){
					return res.json();
				}
			}).then((result) => {
				localStorage.setItem("access_token", result.accessToken);
				httpRequest(method, url, body, success, fail);
			}).catch((error) => fail());
		}else{
			return fail();
		}
	})
}

