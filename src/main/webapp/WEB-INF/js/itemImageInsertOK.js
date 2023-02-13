function thisClose() {
	let itemImage = document.getElementsByName('itemImage')[0].value
	let fileRealname = document.getElementsByName('fileRealname')[0].value
	
	opener.document.getElementById('itemImage').value = itemImage;
	opener.document.getElementById('fileRealname').value = fileRealname;	
	
	self.close();
}

function blankTest() {
	
	let frm = document.getElementById('frm')
	let itemImage = document.getElementById('imgFile').value 
	let itemName = document.getElementById('itemName').value 
	let itemPrice = document.getElementById('itemPrice').value 

	if (itemImage.trim() == null || itemImage.trim() == "") {
		alert('상품 사진을 업로드해주세요.')
		return false;
	} 
	
	if (itemName.trim() == null || itemName.trim() == "") {
		alert('상품 이름을 작성해주세요.')
		return false;
	} 
	
	if (itemPrice.trim() == null || itemPrice.trim() == "") {
		alert('상품 가격을 작성해주세요.')
		return false;
	} else if (isNaN(itemPrice)){
		alert('상품 가격을 숫자로 입력해주세요.')
		return false;
	}
	
	let cfm = confirm('상품을 등록하시겠습니까?')
	if (cfm) {
		frm.submit();		
	} else {
		return false;
	}
	
}