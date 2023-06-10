const selectMember = (member) => {
    const { email, password } = member;
    const string = `${email}:${password}`;
    localStorage.setItem('credentials', btoa(string));
    alert(`${email} 사용자로 설정 했습니다.`);
}

const generateCoupon = (member) => {
    const { email, password } = member;

    const requestData = 1;

    axios.post(`/coupon`, requestData, {
        auth: {
            username: email,
            password: password
        },
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        console.log(response.data);
        console.log(response.status);
    })
    .catch(error => {
        console.error(error);
    });

    alert(`${email} 쿠폰 발급 완료`);
}
