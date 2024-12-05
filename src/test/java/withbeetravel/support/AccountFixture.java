package withbeetravel.support;

import withbeetravel.domain.Account;
import withbeetravel.domain.Product;
import withbeetravel.domain.User;

public class AccountFixture {

    private Long id;
    private User user;
    private String accountNumber = "1111111111111";
    private long balance = 1000000L;
    private Product product = Product.WON통장;
    private boolean isConnectedWibeeCard = false;

    public static AccountFixture builder() {
        return new AccountFixture();
    }

    public AccountFixture id(Long id) {
        this.id = id;
        return this;
    }

    public AccountFixture user(User user) {
        this.user = user;
        return this;
    }

    public AccountFixture accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public AccountFixture balance(long balance) {
        this.balance = balance;
        return this;
    }

    public AccountFixture product(Product product) {
        this.product = product;
        return this;
    }

    public AccountFixture isConnectedWibeeCard(boolean isConnectedWibeeCard) {
        this.isConnectedWibeeCard = isConnectedWibeeCard;
        return this;
    }

    public Account build() {
        return Account.builder()
                .id(id)
                .user(user)
                .accountNumber(accountNumber)
                .balance(balance)
                .product(product)
                .isConnectedWibeeCard(isConnectedWibeeCard)
                .build();
    }
}

