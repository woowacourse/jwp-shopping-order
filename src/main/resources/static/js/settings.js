const selectMember = (member) => {
    const { email, grade, password } = member;
    const string = `${email}:${grade}:${password}`;
    localStorage.setItem('credentials', btoa(string));
    alert(`${email} 사용자로 설정 했습니다.`);
}
