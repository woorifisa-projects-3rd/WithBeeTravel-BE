package withbeetravel.service.payment;

import withbeetravel.domain.Category;

public interface SharedPaymentCategoryClassificationService {

    public Category getCategory(String storeName);
}
