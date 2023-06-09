const selectMember = (member) => {
    const { name, password } = member;
    const string = `${name}:${password}`;
    localStorage.setItem('credentials', btoa(string));
    alert(`${name} 사용자로 설정 했습니다.`);
}
