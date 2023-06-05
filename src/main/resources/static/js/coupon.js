const modal = document.getElementById('modal');

const showAddModal = () => {
    modal.dataset.formType = 'add';
    modal.style.display = 'block';
};

const showEditModal = (coupon) => {
    const elements = modal.getElementsByTagName('input');
    for (const element of elements) {
        element.value = coupon[element.getAttribute('name')];

    }
    modal.dataset.formType = 'edit';
    modal.dataset.couponId = coupon.id;
    modal.style.display = 'block';
};

const hideAddModal = () => {
    modal.style.display = 'none';
    const elements = modal.getElementsByTagName('input');
    for (const element of elements) {
        element.value = '';
    }
}

const form = document.getElementById('form');

form.addEventListener('submit', (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    let coupon = {};
    for (const entry of formData.entries()) {
        const [key, value] = entry;
        coupon[key] = value;
    }

    if (modal.dataset.formType === 'edit') {
        coupon['id'] = modal.dataset.couponId;
        updateCoupon(coupon);
        return;
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

const updateCoupon = (coupon) => {
    const {id} = coupon;
    axios.put(`/coupons/${id}`, coupon)
        .then((response) => {
            window.location.reload();
        })
        .catch((error) => {
            console.error(error);
        });
};

const deleteCoupon = (id) => {
    axios.delete(`/coupons/${id}`)
        .then((response) => {
            window.location.reload();
        })
        .catch((error) => {
            console.error(error);
        });
};

const addCoupon = (id) => {
    const credentials = localStorage.getItem('credentials');

    if (!credentials) {
        alert('사용자 정보가 없습니다.');
        window.location.href = '/coupons';
        return;
    }

    axios.request({
        url: '/coupons/' + id,
        method: "POST",
        headers: {
            'Authorization': `Basic ${credentials}`,
            "Content-Type": "application/json"
        },
    }).then((response) => {
        window.location.reload();
    })
        .catch((error) => {
            console.error(error);
        });
};

