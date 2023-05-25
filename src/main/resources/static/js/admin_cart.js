const deleteProduct = (id) => {
    axios.delete(`/admin/cart/${id}`)
        .then((response) => {
            window.location.reload();
        })
        .catch((error) => {
            console.error(error);
        });
};
