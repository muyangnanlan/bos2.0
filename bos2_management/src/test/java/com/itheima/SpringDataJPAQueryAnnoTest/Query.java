package com.itheima.SpringDataJPAQueryAnnoTest;

public interface Query {
	@MyQuery(name = "from Customer")
	void save();
}
