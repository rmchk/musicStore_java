let editingProductId = null; // Переменная для хранения ID редактируемого товара

// Проверка доступа к админ-панели
async function checkAdminAccess() {
    try {
        const response = await fetch('http://localhost:8080/api/users/me', {
            credentials: 'include',
        });
        if (response.ok) {
            const user = await response.json();
            if (user.role !== 'ADMIN') {
                alert('Доступ запрещён. Только для администраторов.');
                window.location.href = '/index.html';
            }
        } else {
            window.location.href = '/login.html';
        }
    } catch (error) {
        console.error('Error checking admin access:', error);
        window.location.href = '/login.html';
    }
}

// Загрузка списка товаров
async function loadProducts() {
    try {
        const response = await fetch('http://localhost:8080/api/public/products', {
            credentials: 'include',
        });
        const products = await response.json();
        const productsList = document.getElementById('products-list');
        productsList.innerHTML = '';

        products.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>${product.price}</td>
                <td><img src="${product.image || 'images/placeholder.jpg'}" alt="${product.name}" style="width: 50px;"></td>
                <td>
                    <button onclick="editProduct(${product.id})">Редактировать</button>
                    <button onclick="deleteProduct(${product.id})">Удалить</button>
                </td>
            `;
            productsList.appendChild(row);
        });
    } catch (error) {
        console.error('Error loading products:', error);
    }
}

// Загрузка списка пользователей
async function loadUsers() {
    try {
        const response = await fetch('http://localhost:8080/api/admin/users', {
            credentials: 'include',
        });
        if (response.ok) {
            const users = await response.json();
            const usersList = document.getElementById('users-list');
            usersList.innerHTML = '';

            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.email}</td>
                    <td>
                        <select onchange="changeUserRole(${user.id}, this.value)">
                            <option value="USER" ${user.role === 'USER' ? 'selected' : ''}>USER</option>
                            <option value="ADMIN" ${user.role === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                        </select>
                    </td>
                    <td>
                        <button onclick="deleteUser(${user.id})">Удалить</button>
                    </td>
                `;
                usersList.appendChild(row);
            });
        } else {
            console.error('Failed to load users, status:', response.status);
        }
    } catch (error) {
        console.error('Error loading users:', error);
    }
}

// Загрузка метрик
async function loadMetrics() {
    try {
        // Загрузка количества заказов
        const ordersResponse = await fetch('http://localhost:8080/api/admin/orders/metrics/orders-by-user', {
            credentials: 'include',
        });
        if (ordersResponse.ok) {
            const ordersData = await ordersResponse.json();
            renderOrdersCountChart(ordersData);
        } else {
            console.error('Failed to load orders metrics, status:', ordersResponse.status);
        }

        // Загрузка общей суммы заказов
        const priceResponse = await fetch('http://localhost:8080/api/admin/orders/metrics/total-price-by-user', {
            credentials: 'include',
        });
        if (priceResponse.ok) {
            const priceData = await priceResponse.json();
            renderTotalPriceChart(priceData);
        } else {
            console.error('Failed to load total price metrics, status:', priceResponse.status);
        }
    } catch (error) {
        console.error('Error loading metrics:', error);
    }
}

// Отрисовка графика количества заказов
function renderOrdersCountChart(data) {
    const ctx = document.getElementById('ordersCountChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: data.map(item => item.username),
            datasets: [{
                label: 'Количество заказов',
                data: data.map(item => item.orderCount),
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Количество заказов'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Пользователь'
                    }
                }
            }
        }
    });
}

// Отрисовка графика общей суммы заказов
function renderTotalPriceChart(data) {
    const ctx = document.getElementById('totalPriceChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: data.map(item => item.username),
            datasets: [{
                label: 'Общая сумма заказов (руб.)',
                data: data.map(item => item.totalPrice),
                backgroundColor: 'rgba(153, 102, 255, 0.2)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Сумма (руб.)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Пользователь'
                    }
                }
            }
        }
    });
}

// Показать форму добавления/редактирования товара
function showAddProductForm(isEditing = false) {
    const formContainer = document.getElementById('add-product-form');
    const form = document.getElementById('product-form');
    let formTitle = document.getElementById('product-form-title');

    if (!formContainer || !form) {
        console.error('Critical form elements not found:', {
            formContainer: !!formContainer,
            form: !!form
        });
        alert('Ошибка: Форма для редактирования товара не найдена.');
        return;
    }

    // Удаляем дублирующиеся заголовки, если они есть
    const existingTitles = formContainer.querySelectorAll('h4[id="product-form-title"]');
    if (existingTitles.length > 1) {
        console.log(`Found ${existingTitles.length} title elements, removing duplicates`);
        for (let i = 1; i < existingTitles.length; i++) {
            existingTitles[i].remove();
        }
    }

    // Если заголовок не найден, создаём новый
    if (!formTitle) {
        console.log('Creating new form title');
        formTitle = document.createElement('h4');
        formTitle.id = 'product-form-title';
        formContainer.insertBefore(formTitle, form);
    }

    // Обновляем текст заголовка
    formTitle.textContent = isEditing ? 'Редактировать товар' : 'Добавить новый товар';

    formContainer.style.display = 'block';
    form.reset();
    editingProductId = null; // Сбрасываем ID редактируемого товара
}

// Скрыть форму добавления/редактирования товара
function hideAddProductForm() {
    const formContainer = document.getElementById('add-product-form');
    const form = document.getElementById('product-form');
    if (formContainer && form) {
        formContainer.style.display = 'none';
        form.reset();
        editingProductId = null;
    }
}

// Редактирование товара
async function editProduct(productId) {
    try {
        const response = await fetch(`http://localhost:8080/api/public/products/${productId}`, {
            credentials: 'include',
        });
        if (response.ok) {
            const product = await response.json();
            showAddProductForm(true); // Передаем true для режима редактирования
            const form = document.getElementById('product-form');
            form.elements['name'].value = product.name;
            form.elements['price'].value = product.price;
            form.elements['category'].value = product.category || '';
            form.elements['brand'].value = product.brand || '';
            form.elements['description'].value = product.description || '';
            form.elements['image'].value = product.image || '';
            editingProductId = productId;
        } else {
            const errorText = await response.text();
            alert(`Ошибка при загрузке данных товара: ${errorText}`);
        }
    } catch (error) {
        console.error('Error loading product for edit:', error);
        alert('Произошла ошибка при загрузке данных товара. Пожалуйста, попробуйте снова.');
    }
}

// Добавление/редактирование товара
document.getElementById('product-form').addEventListener('submit', async (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    const productData = {
        name: formData.get('name'),
        price: parseFloat(formData.get('price')),
        image: formData.get('image') || null,
        category: formData.get('category') || null,
        brand: formData.get('brand') || null,
        description: formData.get('description') || null,
        inStock: true,
    };

    try {
        const url = editingProductId
            ? `http://localhost:8080/api/admin/products/${editingProductId}`
            : 'http://localhost:8080/api/admin/products';
        const method = editingProductId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(productData),
        });

        if (response.ok) {
            alert(editingProductId ? 'Товар успешно обновлён!' : 'Товар успешно добавлен!');
            hideAddProductForm();
            loadProducts();
        } else {
            const errorText = await response.text();
            alert(`Ошибка: ${errorText}`);
        }
    } catch (error) {
        console.error('Error saving product:', error);
        alert('Произошла ошибка. Пожалуйста, попробуйте снова.');
    }
});

// Удаление товара
async function deleteProduct(productId) {
    if (confirm('Вы уверены, что хотите удалить этот товар?')) {
        try {
            const response = await fetch(`http://localhost:8080/api/admin/products/${productId}`, {
                method: 'DELETE',
                credentials: 'include',
            });
            if (response.ok) {
                alert('Товар удалён!');
                loadProducts();
            } else {
                const errorText = await response.text();
                alert('Ошибка при удалении товара: ' + errorText);
            }
        } catch (error) {
            console.error('Error deleting product:', error);
            alert('Произошла ошибка. Пожалуйста, попробуйте снова.');
        }
    }
}

// Изменение роли пользователя
async function changeUserRole(userId, newRole) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/users/${userId}/role`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify({ role: newRole }),
        });
        if (response.ok) {
            alert('Роль пользователя изменена!');
            loadUsers();
        } else {
            const errorText = await response.text();
            alert('Ошибка при изменении роли: ' + errorText);
        }
    } catch (error) {
        console.error('Error changing user role:', error);
        alert('Произошла ошибка. Пожалуйста, попробуйте снова.');
    }
}

// Удаление пользователя
async function deleteUser(userId) {
    if (confirm('Вы уверены, что хотите удалить этого пользователя?')) {
        try {
            const response = await fetch(`http://localhost:8080/api/admin/users/${userId}`, {
                method: 'DELETE',
                credentials: 'include',
            });
            if (response.ok) {
                alert('Пользователь удалён!');
                loadUsers();
            } else {
                const errorText = await response.text();
                alert('Ошибка при удалении пользователя: ' + errorText);
            }
        } catch (error) {
            console.error('Error deleting user:', error);
            alert('Произошла ошибка. Пожалуйста, попробуйте снова.');
        }
    }
}

// Загружаем данные при загрузке страницы
window.onload = () => {
    checkAdminAccess();
    loadProducts();
    loadUsers();
    loadMetrics();
};