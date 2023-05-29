const percentForm = document.getElementById('percentForm');

percentForm.addEventListener('submit', (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    let coupon = {};
    for (const entry of formData.entries()) {
        const [key, value] = entry;
        coupon[key] = value;
    }

    createCoupon(coupon);
});

const createCoupon = (coupon) => {
    axios.post('/coupons', coupon)
        .then((response) => {
            window.location.reload();
        })
        .catch((error) => {
            console.error(error);
        });
};
