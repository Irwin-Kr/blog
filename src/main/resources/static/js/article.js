const deleteBtn = document.getElementById('delete-btn');

if(deleteBtn) {
	deleteBtn.addEventListener('click', event => {
		let id = document.getElementById('article-id').value;
		fetch(`/articles/${id}`, {
			method: 'DELETE'
		}).then(() =>{
			alert('게시글이 삭제 되었습니다.');
			location.replace('/article');
		});
	});
}

const updateBtn = document.getElementById('update-btn');

if(updateBtn){
	updateBtn.addEventListener('click', evnet =>{
		let params = new URLSearchParams(location.search);
		let id = params.get('id');
		
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
	});
}