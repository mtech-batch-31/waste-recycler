package com.mtech.recycler.model;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRequest {
    private String email;
    private int record;
    private String collectionStatus;
    private String contactPerson;
    private String contactNumber;
    private String collectionDate;
}
