package withbeetravel.domain;

import lombok.Getter;

@Getter
public enum Country {
    US("미국", "USD"),
    ES("스페인", "EUR"),
    FR("프랑스", "EUR"),
    DE("독일", "EUR"),
    IT("이탈리아", "EUR"),
    NL("네덜란드", "EUR"),
    BE("벨기에", "EUR"),
    AT("오스트리아", "EUR"),
    GR("그리스", "EUR"),
    PT("포르투갈", "EUR"),
    IE("아일랜드", "EUR"),
    JP("일본", "JPY"),
    CN("중국", "CNY"),
    KR("한국", "KRW"),
    GB("영국", "GBP"),
    AU("호주", "AUD"),
    CA("캐나다", "CAD"),
    NZ("뉴질랜드", "NZD"),
    MX("멕시코", "MXN"),
    IN("인도", "INR"),
    BR("브라질", "BRL"),
    ZA("남아프리카공화국", "ZAR"),
    SE("스웨덴", "SEK"),
    NO("노르웨이", "NOK"),
    DK("덴마크", "DKK"),
    CH("스위스", "CHF"),
    HK("홍콩", "HKD"),
    SG("싱가포르", "SGD"),
    TH("태국", "THB"),
    TR("터키", "TRY"),
    MY("말레이시아", "MYR"),
    PH("필리핀", "PHP"),
    AE("아랍에미리트", "AED"),
    SA("사우디아라비아", "SAR"),
    KW("쿠웨이트", "KWD"),
    BH("바레인", "BHD"),
    QA("카타르", "QAR"),
    OM("오만", "OMR"),
    JO("요르단", "JOD"),
    LB("레바논", "LBP"),
    EG("이집트", "EGP"),
    ID("인도네시아", "IDR"),
    PK("파키스탄", "PKR"),
    TW("대만", "TWD"),
    VN("베트남", "VND"),
    CO("콜롬비아", "COP"),
    PE("페루", "PEN"),
    CL("칠레", "CLP"),
    AR("아르헨티나", "ARS");

    private final String countryName;

    private final String currencyCode;

    Country(String countryName, String currencyCode) {
        this.countryName = countryName;
        this.currencyCode = currencyCode;
    }



}
