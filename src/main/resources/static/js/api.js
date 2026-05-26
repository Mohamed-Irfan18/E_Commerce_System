const API_BASE = '';

const Auth = {
  getToken() {
    return localStorage.getItem('token');
  },
  setSession(data) {
    localStorage.setItem('token', data.token);
    localStorage.setItem('email', data.email);
    localStorage.setItem('fullName', data.fullName);
    localStorage.setItem('role', data.role);
  },
  clear() {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    localStorage.removeItem('fullName');
    localStorage.removeItem('role');
  },
  isLoggedIn() {
    return !!this.getToken();
  },
  isAdmin() {
    return localStorage.getItem('role') === 'ADMIN';
  },
  getUser() {
    return {
      email: localStorage.getItem('email'),
      fullName: localStorage.getItem('fullName'),
      role: localStorage.getItem('role'),
    };
  },
};

async function api(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...options.headers };
  const token = Auth.getToken();
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const res = await fetch(API_BASE + path, { ...options, headers });
  const text = await res.text();
  let data = null;
  try {
    data = text ? JSON.parse(text) : null;
  } catch {
    data = { error: text || 'Request failed' };
  }

  if (!res.ok) {
    const msg = data?.error || data?.errors || `Error ${res.status}`;
    throw new Error(typeof msg === 'object' ? JSON.stringify(msg) : msg);
  }
  return data;
}

function formatPrice(amount) {
  return '$' + Number(amount).toFixed(2);
}

function showAlert(containerId, message, type = 'error') {
  const el = document.getElementById(containerId);
  if (!el) return;
  el.innerHTML = `<div class="alert alert-${type}">${message}</div>`;
  setTimeout(() => { el.innerHTML = ''; }, 5000);
}

function renderNavbar(activePage) {
  const user = Auth.getUser();
  const loggedIn = Auth.isLoggedIn();
  const isAdmin = Auth.isAdmin();

  let authLinks = '';
  if (loggedIn) {
    authLinks = `
      <span class="user-pill">👤 <strong>${(user.fullName || user.email).split(' ')[0]}</strong></span>
      ${isAdmin ? '<a href="admin.html" class="nav-link"><span class="icon">⚙️</span> Admin</a>' : ''}
      <a href="orders.html" class="nav-link ${activePage === 'orders' ? 'active' : ''}"><span class="icon">📦</span> Orders</a>
      <a href="cart.html" class="nav-link cart-link ${activePage === 'cart' ? 'active' : ''}"><span class="icon">🛒</span> Cart</a>
      <a href="#" id="logoutBtn" class="nav-link"><span class="icon">🚪</span> Logout</a>
    `;
  } else {
    authLinks = `
      <a href="login.html" class="nav-link ${activePage === 'login' ? 'active' : ''}"><span class="icon">👤</span> Login</a>
      <a href="register.html" class="btn btn-accent btn-sm">Sign Up</a>
    `;
  }

  const nav = document.getElementById('navbar');
  if (!nav) return;

  nav.innerHTML = `
    <div class="navbar-wrap">
      <div class="navbar">
        <a href="index.html" class="logo">Shop<span>Hub</span></a>
        <form class="nav-search" id="navSearchForm" onsubmit="return false">
          <input type="text" id="navSearchInput" placeholder="Search for products, brands...">
          <button type="submit" id="navSearchBtn">🔍</button>
        </form>
        <div class="nav-links">
          <a href="index.html" class="nav-link ${activePage === 'shop' ? 'active' : ''}"><span class="icon">🏠</span> Home</a>
          ${authLinks}
        </div>
      </div>
    </div>
  `;

  document.getElementById('logoutBtn')?.addEventListener('click', (e) => {
    e.preventDefault();
    Auth.clear();
    window.location.href = 'index.html';
  });

  document.getElementById('navSearchForm')?.addEventListener('submit', () => {
    const q = document.getElementById('navSearchInput').value.trim();
    if (window.location.pathname.endsWith('index.html') || window.location.pathname === '/') {
      document.getElementById('searchInput').value = q;
      if (typeof loadProducts === 'function') loadProducts();
    } else {
      window.location.href = 'index.html?search=' + encodeURIComponent(q);
    }
  });
}

function requireAuth() {
  if (!Auth.isLoggedIn()) {
    window.location.href = 'login.html?redirect=' + encodeURIComponent(window.location.pathname);
  }
}

function requireAdmin() {
  requireAuth();
  if (!Auth.isAdmin()) {
    window.location.href = 'index.html';
  }
}
