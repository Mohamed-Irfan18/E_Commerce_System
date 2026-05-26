const PRODUCT_IMAGES = {
  'Wireless Headphones': 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&q=80',
  'Smart Watch': 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&q=80',
  'Cotton T-Shirt': 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=600&q=80',
  'Denim Jeans': 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=600&q=80',
  'Java Programming Book': 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&q=80',
};

const CATEGORY_IMAGES = {
  'Electronics': 'https://images.unsplash.com/photo-1498049794561-7780e7231661?w=600&q=80',
  'Clothing': 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=600&q=80',
  'Books': 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=600&q=80',
};

function getProductImage(product) {
  if (product.imageUrl && !product.imageUrl.includes('placehold.co')) {
    return product.imageUrl;
  }
  return PRODUCT_IMAGES[product.name] ||
    CATEGORY_IMAGES[product.categoryName] ||
    'https://images.unsplash.com/photo-1472851294601-062f824d29cc?w=600&q=80';
}

function getCategoryImage(name) {
  return CATEGORY_IMAGES[name] ||
    'https://images.unsplash.com/photo-1472851294601-062f824d29cc?w=600&q=80';
}

function fakeRating(name) {
  const hash = name.split('').reduce((a, c) => a + c.charCodeAt(0), 0);
  const rating = (4 + (hash % 10) / 10).toFixed(1);
  const reviews = 50 + (hash % 450);
  return { rating, reviews };
}

function renderStars(rating) {
  const full = Math.floor(rating);
  const half = rating % 1 >= 0.5;
  let stars = '★'.repeat(full);
  if (half) stars += '½';
  stars += '☆'.repeat(5 - full - (half ? 1 : 0));
  return stars.substring(0, 5).replace('½', '★');
}

function showToast(message, type = 'success') {
  const existing = document.querySelector('.toast');
  if (existing) existing.remove();
  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.textContent = message;
  document.body.appendChild(toast);
  setTimeout(() => toast.remove(), 3000);
}

function renderTopBar() {
  const el = document.getElementById('topBar');
  if (!el) return;
  el.innerHTML = `
    <div class="top-bar">
      🎉 Free shipping on orders over $50 &nbsp;|&nbsp;
      <span>Use code <strong>WELCOME10</strong> for 10% off your first order!</span>
    </div>
  `;
}

function renderFooter() {
  const el = document.getElementById('footer');
  if (!el) return;
  el.innerHTML = `
    <footer class="site-footer">
      <div class="footer-grid">
        <div class="footer-brand">
          <a href="index.html" class="logo">Shop<span>Hub</span></a>
          <p>Your one-stop destination for electronics, fashion, books and more. Quality products, fast delivery, secure checkout.</p>
        </div>
        <div class="footer-col">
          <h4>Shop</h4>
          <a href="index.html">All Products</a>
          <a href="index.html#categories">Categories</a>
          <a href="cart.html">Shopping Cart</a>
        </div>
        <div class="footer-col">
          <h4>Account</h4>
          <a href="login.html">Login</a>
          <a href="register.html">Register</a>
          <a href="orders.html">My Orders</a>
        </div>
        <div class="footer-col">
          <h4>Support</h4>
          <a href="#">Help Center</a>
          <a href="#">Returns</a>
          <a href="#">Contact Us</a>
        </div>
      </div>
      <div class="footer-bottom">
        © 2026 ShopHub E-Commerce. Built with Java & Spring Boot.
      </div>
    </footer>
  `;
}

function initLayout() {
  renderTopBar();
  renderFooter();
}
