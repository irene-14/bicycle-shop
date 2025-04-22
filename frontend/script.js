/**
 * Marcus Bike Shop
 * 
 * This code handles the dynamic loading of bike parts options,
 * calculates pricing in real-time, and manages the shopping cart functionality.
 * 
 * Author: Irene Truchado Mazzoli
 */

// Global state to store the product data fetched from the API
let priceData = {};

/**
 * Fetches product data from the API and populates the selection dropdowns
 */
async function loadOptions() {
  try {
    // Fetch product data from the API endpoint
    const response = await fetch("http://localhost:8080/products");
    priceData = await response.json();
    const parts = priceData.parts;
    
    // Iterate through each bike part category
    for (const partKey in parts) {
      const select = document.getElementById(partKey);
      if (!select) continue; // Skip if element doesn't exist in the DOM
      
      // Create and append the default option
      const defaultOption = document.createElement('option');
      defaultOption.value = '';
      defaultOption.text = 'Please choose...';
      select.appendChild(defaultOption);
      
      // Create options for each part variation with pricing
      const options = parts[partKey].options;
      for (const optionKey in options) {
        const price = options[optionKey];
        const option = document.createElement('option');
        option.value = optionKey;
        
        // Format the option text with proper capitalization and price
        option.text = `${optionKey.charAt(0).toUpperCase() + optionKey.slice(1)} - €${price}`;
        select.appendChild(option);
      }
    }
  } catch (error) {
    console.error("Error loading options:", error);
    // In a production environment, we could implement user-facing error handling here
  }
}

/**
 * Initialize event listeners when the DOM is fully loaded
 * Sets up change detection on all part selection dropdowns
 */
document.addEventListener("DOMContentLoaded", function() {
  const formElements = document.querySelectorAll(".part-selection");
  
  // Add change event listener to each form element
  formElements.forEach(element => {
    element.addEventListener("change", function() {
      updateCart();
      checkAllSelections();
    });
  });
  
  // Initial validation check on page load
  checkAllSelections();
});

/**
 * Calculates the total price based on all selected bike parts
 * Updates the UI to display the current total
 */
function updateCart() {
  let totalPrice = 0;
  
  // Iterate through all part selections and sum their prices
  document.querySelectorAll(".part-selection").forEach((part) => {
    const selectedOption = part.value;
    if (selectedOption) {
      // Get the price for this selected part option
      const price = getPartPrice(part.name, selectedOption);
      totalPrice += price;
    }
  });
  
  // Update the UI with the new total
  updateTotalPrice(totalPrice);
}

/**
 * Retrieves the price for a specific part and option from the data
 * @param {string} partName - The name/ID of the bike part
 * @param {string} optionKey - The selected option for the part
 * @return {number} - The price of the selected option
 */
function getPartPrice(partName, optionKey) {
  if (priceData.parts[partName] && priceData.parts[partName].options[optionKey] !== undefined) {
    return priceData.parts[partName].options[optionKey];
  }
  return 0; // Default to zero if price not found
}

/**
 * Updates the total price display in the UI
 * @param {number} total - The calculated total price
 */
function updateTotalPrice(total) {
  const totalPriceElement = document.getElementById("totalPrice");
  totalPriceElement.textContent = `${total.toFixed(2)}`; // Format with 2 decimal places
}

/**
 * Validates the form to ensure all selections have been made
 * Enables/disables the "Add to Cart" button accordingly
 */
function checkAllSelections() {
  const selects = document.querySelectorAll('.part-selection');
  let allSelected = true;
  
  // Check each select element for a value
  selects.forEach(select => {
    if (!select.value) {
      allSelected = false;
    }
  });
  
  const addToCartButton = document.querySelector('.add-to-cart');
  
  // Toggle button state based on selection status
  if (allSelected) {
    addToCartButton.disabled = false;
    addToCartButton.classList.remove('disabled');
  } else {
    addToCartButton.disabled = true;
    addToCartButton.classList.add('disabled');
  }
}

/**
 * Resets the form, clearing all selections and price display
 * Used when the user wants to start over
 */
function clearSelection() {
  document.querySelectorAll('.part-selection').forEach(select => {
    select.selectedIndex = 0;
  });
  
  document.getElementById('totalPrice').textContent = '0';
  checkAllSelections();
}

// Initialize the application by loading the product data
loadOptions();

/**
 * Adds the current bike configuration to the cart
 * Called when the "Add to Cart" button is clicked
 */
function addToCart() {
  // Collect all selected options
  const selectedOptions = {};
  document.querySelectorAll(".part-selection").forEach((part) => {
    if (part.value) {
      selectedOptions[part.name] = part.value;
    }
  });
  
  // Get the total price
  const totalPrice = parseFloat(document.getElementById("totalPrice").textContent);
  
  // For now, just show a confirmation message
  alert(`Bike added to cart! Total: €${totalPrice.toFixed(2)}`);
  
  // Reset the form after adding to cart
  clearSelection();
}