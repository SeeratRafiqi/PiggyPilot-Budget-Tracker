document.addEventListener('DOMContentLoaded', function() {
    loadTransactions();
    loadCategories();
});
//function for loading the transaction
function loadTransactions() {
    fetch('/api/transactions')
        .then(response => response.json())
        .then(data => {
            transactions = data;
            renderTransactions();
        })
        .catch(error => console.error('Error loading transactions:', error));
}

// function loadCategories() {
//     fetch('/api/groups')
//         .then(response => response.json())
//         .then(data => {
//             const categorySelect = document.getElementById('category');
//             categorySelect.innerHTML = ''; // Clear previous options
//             data.forEach(group => {
//                 const option = document.createElement('option');
//                 option.value = group.budgetId;
//                 option.text = group.category;
//                 categorySelect.appendChild(option);
//             });
//         })
//         .catch(error => console.error('Error loading categories:', error));
// }

function renderTransactions() {
    const tbody = document.getElementById('transaction-table').querySelector('tbody');
    const rows = [];

    transactions.forEach((transaction, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${index + 1}</td>
            <td>${transaction.note}</td>
            <td>${transaction.group.category}</td>
            <td>${transaction.date}</td>
            <td>${transaction.amount}</td>
            <td>${transaction.paymentMethod}</td>
        `;
        row.addEventListener('click', function() {
            openModal('edit', transaction.transactionId);
        });
        rows.push(row);
    });

    tbody.innerHTML = ''; // Clear previous content
    rows.forEach(row => tbody.appendChild(row));
}

function openModal(action, transactionId = null) {
    currentAction = action;
    currentTransactionId = transactionId;

    if (action === 'edit' && transactionId !== null) {
        const transaction = transactions.find(t => t.transactionId === transactionId);
        if (transaction) {
            document.getElementById('transactionId').value = transactionId;
            document.getElementById('note').value = transaction.note;
            document.getElementById('category').value = transaction.group.budgetId;
            document.getElementById('date').value = transaction.date;
            document.getElementById('amount').value = transaction.amount;
            document.getElementById('paymentMethod').value = transaction.paymentMethod;
        }
        document.getElementById('deleteButton').style.display = 'inline-block';
    } else {
        document.getElementById('transactionForm').reset();
        document.getElementById('transactionId').value = '';
        document.getElementById('deleteButton').style.display = 'none';
    }

    document.getElementById('transactionModal').style.display = 'block';
}

function closeModal() {
    document.getElementById('transactionModal').style.display = 'none';
}

document.getElementById('transactionForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const id = document.getElementById('transactionId').value || null;
    const note = document.getElementById('note').value;
    const category = document.getElementById('category').value;
    const date = document.getElementById('date').value;
    const amount = document.getElementById('amount').value;
    const paymentMethod = document.getElementById('paymentMethod').value;

    const transaction = { transactionId: id, note, date, amount, paymentMethod, group: { budgetId: category } };

    const url = currentAction === 'edit' ? `/api/transactions/${id}` : '/api/transactions';
    const method = currentAction === 'edit' ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(transaction)
    })
        .then(response => response.json())
        .then(data => {
            if (currentAction === 'edit') {
                const index = transactions.findIndex(t => t.transactionId === currentTransactionId);
                transactions[index] = data;
            } else {
                transactions.push(data);
            }
            renderTransactions();
            closeModal();
        })
        .catch(error => console.error('Error saving transaction:', error));
});

function deleteTransaction() {
    if (currentTransactionId !== null) {
        fetch(`/api/transactions/${currentTransactionId}`, {
            method: 'DELETE'
        })
            .then(() => {
                transactions = transactions.filter(t => t.transactionId !== currentTransactionId);
                renderTransactions();
                closeModal();
            })
            .catch(error => console.error('Error deleting transaction:', error));
    } else {
        alert('Please select a transaction to delete.');
    }
}

// ---  Navigation bar functions ---  //
document.addEventListener("DOMContentLoaded", function() {
    let currentUrl = window.location.href;
    document.querySelectorAll('.tab').forEach(tab => {
        if (currentUrl.includes(tab.getAttribute('data-url'))) {
            tab.classList.add('active');
        } else {
            tab.classList.remove('active');
        }
    });
});

function navigateToTab(element) {
    window.location.href = element.getAttribute('data-url');
}

function navigateToDashboard(element) {
    window.location.href = element.getAttribute('data-url');
}

document.addEventListener('DOMContentLoaded', function() {
    loadCategories();
});

function loadCategories() {
    fetch('/getCategories') // Endpoint to fetch categories from the database
        .then(response => response.json())
        .then(data => {
            const categorySelect = document.getElementById('category');
            data.forEach(category => {
                const option = document.createElement('option');
                option.value = category;
                option.text = category;
                categorySelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error loading categories:', error));
}
